#include "../include/DistanceMap.hpp"
#include <boost/graph/floyd_warshall_shortest.hpp>
#include <boost/graph/johnson_all_pairs_shortest.hpp>
#include <boost/graph/graphviz.hpp>
#include <boost/graph/random.hpp>
#include <iostream>
#include <string>
#include <chrono>

#define NUM_VERTICES 10

int D[NUM_VERTICES][NUM_VERTICES];

//Print function for the DistanceMap object
void print_map(DistanceMap& d, Graph& G){
	string out = "\t";
	for(VertexI vi = vertices(G).first; vi != vertices(G).second; ++vi)
			out.append(to_string(*vi)+"\t");
	out.append("\n");
	for(VertexI vi = vertices(G).first; vi != vertices(G).second; ++vi){
		out.append(to_string(*vi)+"\t");
		for(VertexI vj = vertices(G).first; vj != vertices(G).second; ++vj){
			if(d.length(*vi, *vj) == numeric_limits<int>::max())
				out.append("-\t");
			else
				out.append(to_string(d.length(*vi, *vj))+"\t");
		}
	out.append("\n");
	}
	cout<<out<<endl;
}
//Respective function for D distance matrix used for Johnson and Floyd Warshall
void print_map(Graph& G){
	string out = "\t";
	for(VertexI vi = vertices(G).first; vi != vertices(G).second; ++vi)
			out.append(to_string(*vi)+"\t");
	out.append("\n");
	for(VertexI vi = vertices(G).first; vi != vertices(G).second; ++vi){
		out.append(to_string(*vi)+"\t");
		for(VertexI vj = vertices(G).first; vj != vertices(G).second; ++vj){
			if(D[*vi][*vj] == numeric_limits<int>::max())
				out.append("-\t");
			else
				out.append(to_string(D[*vi][*vj])+"\t");
		}
	out.append("\n");
	}
	cout<<out<<endl;
}
//Check if all elements of DistanceMap and D (outcome of either Johnson or Floyd Warshall)
//match, if they dont the DistanceMap algorithm isn't giving the correct results
bool result_check(DistanceMap& d, Graph& G){
	bool res = true;
	for(VertexI vi = vertices(G).first; vi != vertices(G).second; ++vi){
		for(VertexI vj = vertices(G).first; vj != vertices(G).second; ++vj){
			if(D[*vi][*vj] != d.length(*vi, *vj)){
				cout<<"("<<*vi<<", "<<*vj<<")"<<endl;
				res = false;
			}
		}
	}
	return res;
}

int main(int argc, char** argv){
	srand(time(0));
	Graph G;

	int max_weight = 100;
	int min_edges = NUM_VERTICES;
	int max_edges = NUM_VERTICES * (NUM_VERTICES-1);
	//Initialize vertices in graph
	for(int i=0;i<NUM_VERTICES;i++)
		add_vertex(G);

	int num_edges = rand()%(max_edges - min_edges + 1) + min_edges;
	//Add random edges to graph
	for(int i=0;i<num_edges;i++){
		int s = rand()%(NUM_VERTICES-1 - 0 + 1) + 0;
		int t = rand()%(NUM_VERTICES-1 - 0 + 1) + 0;
		int w = rand()%(max_weight - 1 + 1) + 1;
		if(s == t)
			continue;
		if(!edge(s, t, G).second){
			Edge e = add_edge(s, t, G).first;
			put(edge_weight, G, e, w);
		}
	}
	//Run and time functions
	auto start = chrono::monotonic_clock::now();
	bool valid = floyd_warshall_all_pairs_shortest_paths(G, D);
	auto end = chrono::monotonic_clock::now();
	cout <<"Floyd Warshall Shortest Paths: \t"<< chrono::duration_cast<chrono::microseconds>(end - start).count() <<" micro sec"<< endl;

	start = chrono::monotonic_clock::now();
	valid = johnson_all_pairs_shortest_paths(G, D);
	end = chrono::monotonic_clock::now();
	cout <<"Johnson Shortest Paths: \t"<< chrono::duration_cast<chrono::microseconds>(end - start).count() <<" micro sec"<< endl;

	start = chrono::monotonic_clock::now();
	DistanceMap d = DistanceMap(G);
	end = chrono::monotonic_clock::now();
	cout <<"Project Shortest Paths: \t"<< chrono::duration_cast<chrono::microseconds>(end - start).count() <<" micro sec"<< endl; 

	//If results dont match, something is wrong
	if(!result_check(d, G)){
		cout<<"Results Incorrect!"<<endl;
		return 0;
	}

	//add random edge to graph
	int s, t, w;
	while(true){
		s = rand()%(NUM_VERTICES-1 - 0 + 1) + 0;
		t = rand()%(NUM_VERTICES-1 - 0 + 1) + 0;
		w = rand()%(max_weight - 1 + 1) + 1;
		if(s == t || edge(s, t, G).second)
			continue;
		break;
	}
	Edge e = add_edge(s, t, G).first;
	put(edge_weight, G, e, w);
	num_edges++;

	start = chrono::monotonic_clock::now();
	d.add(s, t, w);
	end = chrono::monotonic_clock::now();
	cout <<"Project Add: \t\t\t"<< chrono::duration_cast<chrono::microseconds>(end - start).count() <<" micro sec"<< endl; 

	//get a random node that has at least one out edge, and change that edge's cost, pseudo pick randomly edges
	while(true){
		s = rand()%(NUM_VERTICES-1 - 0 + 1) + 0;
		if(out_degree(s, G) == 0)
			continue;
		e = *(out_edges(s, G).first);
		w = rand()%(get(edge_weight, G, e) - 1 + 1) + 1;
		if(w >= get(edge_weight, G, e))
			continue;
		break;
	}
	int init_cost = get(edge_weight, G, e);
	put(edge_weight, G, e, init_cost - w);
	//call decrease function
	start = chrono::monotonic_clock::now();
	d.decrease(s, t, init_cost, w);
	end = chrono::monotonic_clock::now();
	cout <<"Project decrease: \t\t"<< chrono::duration_cast<chrono::microseconds>(end - start).count() <<" micro sec"<< endl; 
	//choose randomly 2 vertices and look for minpath between them
	while(true){
		s = rand()%(NUM_VERTICES-1 - 0 + 1) + 0;
		t = rand()%(NUM_VERTICES-1 - 0 + 1) + 0;
		if(s == t)
			continue;
		break;
	}
	start = chrono::monotonic_clock::now();
	vector<Vertex> p = d.minpath(s, t);
	end = chrono::monotonic_clock::now();
	cout <<"Project minpath: \t\t"<< chrono::duration_cast<chrono::microseconds>(end - start).count() <<" micro sec"<< endl; 
	//save graph in dot file form
	ofstream dotfile("Graph.dot");
	write_graphviz (dotfile, G, make_label_writer(get(vertex_index, G)), make_label_writer(get(edge_weight, G)));

	return 0;
}
