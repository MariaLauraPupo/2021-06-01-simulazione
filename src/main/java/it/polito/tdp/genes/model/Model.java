package it.polito.tdp.genes.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.genes.db.GenesDao;


public class Model {
	private SimpleWeightedGraph<Genes, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	private Map<String,Genes> idMap;
	
	
	public Model() {
		this.dao = new GenesDao();
		this.idMap = new HashMap<String,Genes>(); 
		//riempio l'idMap
		dao.getAllGenes(idMap);
	}
  public void creaGrafo(){
      this.grafo = new SimpleWeightedGraph<Genes, DefaultWeightedEdge>(DefaultWeightedEdge.class);
      //vertici
      Graphs.addAllVertices(grafo, dao.getGeneVertici(idMap));
      //archi
      for(Arco a: dao.getArchi(idMap)) {
			if(grafo.containsVertex(a.getG1()) && grafo.containsVertex(a.getG2()) ) {
				DefaultWeightedEdge e = this.grafo.getEdge(a.getG1(), a.getG2());
				if(e == null) {
			
				if(a.getG1().getChromosome() != a.getG2().getChromosome()) {
				
					Graphs.addEdgeWithVertices(grafo, a.getG1(), a.getG2(), (a.getPeso()));
				}else if(a.getG1().getChromosome() == a.getG2().getChromosome()) {

					Graphs.addEdgeWithVertices(grafo, a.getG1(), a.getG2(), ((double) 2)*a.getPeso());
				}
			}
		}
      
  }
      System.out.println("vertici " + this.grafo.vertexSet().size());
      System.out.println("archi " + this.grafo.edgeSet().size());

      }
  
  public String getNumeroVerticiArchi() {
	  return "Grafo creato con " + this.grafo.vertexSet().size() + " e " + this.grafo.edgeSet().size();
  }
  
  public Set<Genes> getVertici(){
	  return this.grafo.vertexSet();
  }
  public String getAdiacenti(Genes gene){
	  String stringa = "";
	List<Genes> adiacenti = new LinkedList<Genes>();
	adiacenti.addAll(Graphs.neighborListOf(grafo, gene));
	/*Map<Double, Genes> result = new HashMap<Double, Genes>();
	for(Genes g : adiacenti) {
	result.put(grafo.getEdgeWeight(grafo.getEdge(gene, g)), g);
	}*/
	for(Genes g : adiacenti ) {
		stringa += g.toString() + " " + this.grafo.getEdgeWeight(this.grafo.getEdge(gene, g)) + "\n";
	}
    return stringa;
	  
  }
  

}
