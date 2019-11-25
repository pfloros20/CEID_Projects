#include "../include/DistanceMap.hpp"

DistanceMap::DistanceMap(const Graph& G){
	//For all vertices set root for ANC, DESC
	for(VertexI vi = vertices(G).first; vi != vertices(G).second; ++vi){
		TVertex tva = add_vertex(ANC[*vi]);
		ANC[*vi][tva].vertex = *vi;
		TVertex tvd = add_vertex(DESC[*vi]);
		DESC[*vi][tvd].vertex = *vi;
		for(VertexI vj = vertices(G).first; vj != vertices(G).second; ++vj){
			if(*vi == *vj)
				D[make_pair(*vi, *vj)] = 0;
			else
				D[make_pair(*vi, *vj)] = numeric_limits<int>::max();
			//Initialize FORWARD and BACKWARD maps with -1 value representing null
			FORWARD[make_pair(*vi, *vj)] = -1;
			BACKWARD[make_pair(*vi, *vj)] = -1;
		}
		//Initialize each FORWARD, BACKWARD entry for each node to his self as the root
		//of the DESC and ANC tree respectively
		FORWARD[make_pair(*vi, *vi)] = tvd;
		BACKWARD[make_pair(*vi, *vi)] = tva;
	}
	//For each edge in graph, call the add function for the DistanceMap
	for(EdgeI e = edges(G).first;e != edges(G).second;++e){
		Vertex s = source(*e, G);
		Vertex t = target(*e, G);
		int w = get(edge_weight, G, *e);
		add(s, t, w);
	}
}

void DistanceMap::Update(Vertex x, Vertex i, Vertex j, Tree T, int cost){
	Vertex y, w;
	Tree N;
	queue<Vertex> Q;
	Q.push(j);
	while(!Q.empty()){
		y = Q.front();
		Q.pop();
		if(D[make_pair(x, i)] + cost + D[make_pair(j, y)] < D[make_pair(x, y)]){
			TVertex tv, ptv, pptv;
			Tree& d = DESC[x];
			//Forward
			if(y == j){
				//if node doesnt exist, add new one. If it does remove old connection
				if(FORWARD[make_pair(x, j)] == -1){
					tv = add_vertex(d);
					d[tv].vertex = j;
				}else{
					tv = FORWARD[make_pair(x, j)];
					ptv = FORWARD[make_pair(x, d[tv].parent)];
					remove_edge(ptv, tv, d);
				}
				d[tv].parent = i;
				ptv = FORWARD[make_pair(x, d[tv].parent)];
				add_edge(ptv, tv, d);
				TVertex ntv = add_vertex(N);
				N[ntv].vertex = j;
				FORWARD[make_pair(x, j)] = tv;
			}else{
				//if node doesnt exist, add new one. If it does remove old connection
				if(FORWARD[make_pair(x, y)] == -1){
					tv = add_vertex(d);
					d[tv].vertex = y;
				}else{
					tv = FORWARD[make_pair(x, y)];
					ptv = FORWARD[make_pair(x, d[tv].parent)];
					remove_edge(ptv, tv, d);
				}
				pptv = FORWARD[make_pair(j, y)];
				d[tv].parent = DESC[j][pptv].parent;
				ptv = FORWARD[make_pair(x, d[tv].parent)];
				add_edge(ptv, tv, d);
				TVertex	ntv = add_vertex(N);
				N[ntv].vertex = y;
				N[ntv].parent = DESC[j][pptv].parent;
				ptv = add_vertex(N);
				N[ptv].vertex = N[ntv].parent;
				add_edge(ptv, ntv, N);
				FORWARD[make_pair(x, y)] = tv;	
			}
			Tree& a = ANC[y];
			//Backward
			if(x == i){
				//if node doesnt exist, add new one. If it does remove old connection
				if(BACKWARD[make_pair(y, i)] == -1){
					tv = add_vertex(a);
					a[tv].vertex = i;
				}else{
					tv = BACKWARD[make_pair(y, i)];
					ptv = BACKWARD[make_pair(y, a[tv].parent)];
					remove_edge(ptv, tv, a);
				}
				a[tv].parent = j;
				ptv = BACKWARD[make_pair(y, a[tv].parent)];
				add_edge(ptv, tv, a);
				TVertex ntv = add_vertex(N);
				N[ntv].vertex = i;
				BACKWARD[make_pair(y, i)] = tv;
			}else{
				//if node doesnt exist, add new one. If it does remove old connection
				if(BACKWARD[make_pair(y, x)] == -1){
					tv = add_vertex(a);
					a[tv].vertex = x;
				}else{
					tv = BACKWARD[make_pair(y, x)];
					ptv = BACKWARD[make_pair(y, a[tv].parent)];
					remove_edge(ptv, tv, a);
				}
				pptv = BACKWARD[make_pair(i, x)];
				a[tv].parent = ANC[i][pptv].parent;
				ptv = BACKWARD[make_pair(y, a[tv].parent)];
				add_edge(ptv, tv, a);
				TVertex ntv = add_vertex(N);
				N[ntv].vertex = x;
				N[ntv].parent = ANC[i][pptv].parent;
				ptv = add_vertex(N);
				N[ptv].vertex = N[ntv].parent;
				add_edge(ptv, ntv, N);
				BACKWARD[make_pair(y, x)] = tv;	
			}
			D[make_pair(x, y)] = D[make_pair(x, i)] + cost + D[make_pair(j, y)];
			for(TEdgeI e = edges(T).first; e != edges(T).second; ++e){
				if(T[source(*e, T)].vertex == y){
					w = T[target(*e, T)].vertex;

					Q.push(w);
				}
			}
		}
	}
	if(num_vertices(N) != 0){
		TVertex tx = BACKWARD[make_pair(i, x)]; 
		for(TOEdgeI e = out_edges(tx, ANC[i]).first; e != out_edges(tx, ANC[i]).second; ++e){
			Vertex u = ANC[i][target(*e, ANC[i])].vertex;
			Update(u, i, j, N, D[make_pair(i, j)]);
		}
	}
}

void DistanceMap::add(Vertex x, Vertex y, int w){
	Update(x, x, y, DESC[y], w);
}

void DistanceMap::decrease(Vertex x, Vertex y,int init_cost, int diff){
	add(x, y, init_cost - diff);
}

int DistanceMap::length(Vertex x, Vertex y){
	return D[make_pair(x, y)];
}

vector<Vertex> DistanceMap::minpath(Vertex x, Vertex y){
	//If there is no FORWARD entry there is no path, return empty vector
	if(FORWARD[make_pair(x, y)] == -1)
		return vector<Vertex>();
	vector<Vertex> out;
	Tree& T = DESC[x];
	TVertex root = FORWARD[make_pair(x, x)];
	TVertex current = FORWARD[make_pair(x, y)];
	//Run the tree upwards until you hit the root and save it in out
	while(current != root){
		out.push_back(T[current].vertex);
		current = FORWARD[make_pair(x, T[current].parent)];
	}
	//Add the root to out and reverse the order of the elements in the vector
	out.push_back(T[root].vertex);
	reverse(out.begin(), out.end());
	return out;
}
