package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
	}
	
	//è piu utile creare il grafo in un metodo apposito piuttosto che nel costruttore perchè se inserito nel 
	//costruttore non viene cancellato la volta successiva quindi vengono aggiunti sempre piu grafi
	//chiamandolo con un metodo sono sicuro che il grafo lo creo ogni volta
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta dei vertici
		dao.listObjects(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//aggiunta archi
		//APPROCCIO 1 doppio ciclo for sui vertici
		/*for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				if (!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) {
					int peso = dao.getPeso(a1,a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		} */
		
		
		//APPROCCIO 3
		for(Adiacenza a : dao.getAdiacenze()) {
			 	Graphs.addEdge(this.grafo, idMap.get(a.getId1()),idMap.get(a.getId2()), a.getPeso());
		}
		
		System.out.println("GRAFO CREATO");
		System.out.println("# VERTICI: "+grafo.vertexSet().size());
		System.out.println("# ARCHI: "+grafo.edgeSet().size());
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public ArtObject getObject(int objectId) {
		return idMap.get(objectId);
	}

	
	public int getComponenteConnessa(ArtObject vertice) {
		//Set<ArtObject> visitati = new HashSet<>();
		
		
		
		//dato un vertice dice quali sono i vertici adiacenti
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> ci = 
				new ConnectivityInspector<>(this.grafo);
		
		System.out.println(ci.connectedSetOf(vertice));
		return ci.connectedSetOf(vertice).size();
		
		
		
		/*
		DepthFirstIterator<ArtObject,DefaultWeightedEdge> it = 
				new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(this.grafo, vertice);
		
		visitati = this.grafo.connectedSetsOf(vertice);
		while (it.hasNext()) {
			visitati.add(it.next());
		}
		
		return visitati.size();
	*/
	}
	
}
