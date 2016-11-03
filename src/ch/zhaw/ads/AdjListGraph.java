package ch.zhaw.ads;

import java.util.*;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

public class AdjListGraph<N extends Node<E>, E extends Edge<N>>
	implements Graph<N, E> {

	private final List<N> nodes = new LinkedList<N>();

	// pragma pleaselookaway start
	private Type[] getTypeVariables() {
		ParameterizedType superType =
			(ParameterizedType)AdjListGraph.class.getGenericSuperclass();
		return superType.getActualTypeArguments();
	}

	private N newNode() throws InstantiationException, IllegalAccessException {
		Class<N> clazz = (Class<N>)(this.getTypeVariables()[0]);
		return clazz.newInstance();
	}

	private E newEdge() throws InstantiationException, IllegalAccessException {
		Class<E> clazz = (Class<E>)this.getTypeVariables()[1];
		return clazz.newInstance();
	}
	// pragma pleaselookaway stop

	// füge Knoten hinzu, gebe alten zurück falls Knoten schon existiert
	public N addNode(String name) throws Throwable {
		N node = findNode(name);
		if (node == null) {
			node = this.newNode();
			node.setName(name);
			nodes.add(node);
		}
		return node;
	}

	// füge gerichtete Kante hinzu
	public void addEdge(String source, String dest, double weight)
		throws Throwable {

		N src = addNode(source);
		N dst = addNode(dest);

		try {
			E edge = this.newEdge();
			edge.setDest(dst);
			edge.setWeight(weight);
			src.addEdge(edge);
		} catch (Exception e) {}
	}

	// finde den Knoten anhand seines Namens
	public N findNode(String name) {
		for (N node : nodes) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}

	// Iterator über alle Knoten
	public List<N> getNodes() {
		return nodes;
	}
}
