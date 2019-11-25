#include <boost/graph/adjacency_list.hpp>
#include <limits>
#include <queue>
#include <iostream>
using namespace std;
using namespace boost;

typedef property<boost::edge_weight_t, int> weight_t;
typedef adjacency_list<vecS, vecS, directedS, no_property, weight_t> Graph;
typedef graph_traits<Graph>::vertex_descriptor Vertex;
typedef graph_traits<Graph>::edge_descriptor Edge;
typedef graph_traits<Graph>::vertex_iterator VertexI;
typedef graph_traits<Graph>::edge_iterator EdgeI;
typedef graph_traits<Graph>::out_edge_iterator OEdgeI;

//Each tree node saves the respective vertex value and
//parent value of the graph nodes.
struct tree_properties{
	Vertex vertex;
	Vertex parent;
};

typedef adjacency_list<vecS, vecS, directedS, tree_properties> Tree;
typedef graph_traits<Tree>::vertex_descriptor TVertex;
typedef graph_traits<Tree>::edge_iterator TEdgeI;
typedef graph_traits<Tree>::vertex_iterator TVertexI;
typedef graph_traits<Tree>::out_edge_iterator TOEdgeI;

class DistanceMap{
public:
	DistanceMap(const Graph& G);
	void add(Vertex x, Vertex y, int w);
	void decrease(Vertex x, Vertex y, int init_cost, int diff);
	int length(Vertex x, Vertex y);
	vector<Vertex> minpath(Vertex x, Vertex y);
private:
	map<pair<Vertex, Vertex>, int> D;
	map<pair<Vertex, Vertex>, TVertex> FORWARD;
	map<pair<Vertex, Vertex>, TVertex> BACKWARD;
	map<Vertex, Tree> ANC;
	map<Vertex, Tree> DESC;
	void Update(Vertex x, Vertex i, Vertex j, Tree T, int cost);
};
