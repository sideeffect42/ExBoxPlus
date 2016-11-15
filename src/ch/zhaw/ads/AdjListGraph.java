package ch.zhaw.ads;

import java.util.*;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

public class AdjListGraph<N extends Node, E extends Edge>
	implements Graph<N, E> {

	private final Class<N> nClass;
	private final Class<E> eClass;

	private final List<N> nodes = new LinkedList<N>();

	public AdjListGraph(Class<N> nodeClass, Class<E> edgeClass) {
		this.nClass = nodeClass;
		this.eClass = edgeClass;
	}

	private N newNode() throws IllegalAccessException, InstantiationException {
		return this.nClass.newInstance();
	}

	private E newEdge() throws IllegalAccessException, InstantiationException {
		return this.eClass.newInstance();
	}

	// füge Knoten hinzu, gebe alten zurück falls Knoten schon existiert
	public N addNode(String name) throws Throwable {
		N node = this.findNode(name);
		if (node == null) {
			node = this.newNode();
			node.setName(name);
			this.nodes.add(node);
		}
		return node;
	}

	// füge gerichtete Kante hinzu
	public void addEdge(String source, String dest, double weight)
		throws Throwable {

		N src = this.addNode(source);
		N dst = this.addNode(dest);

		try {
			E edge = this.newEdge();
			edge.setDest(dst);
			edge.setWeight(weight);
			src.addEdge(edge);
		} catch (Exception e) {}
	}

	// finde den Knoten anhand seines Namens
	public N findNode(String name) {
		for (N node : this.nodes) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}

	// Iterator über alle Knoten
	public List<N> getNodes() {
		return this.nodes;
	}
}
