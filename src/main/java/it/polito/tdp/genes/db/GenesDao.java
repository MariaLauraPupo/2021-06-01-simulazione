package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public void getAllGenes(Map<String,Genes> idMap){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
//		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getString("GeneID"))){
				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
	//			result.add(genes);
				idMap.put(res.getString("GeneID"), genes);
				}
				}
			
			res.close();
			st.close();
			conn.close();
//			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
	//		return null;
		}
	}
	
	public List<Genes> getGeneVertici(Map<String,Genes> idMap){
		String sql = "SELECT g.GeneID AS id, g.Essential AS essential, g.Chromosome AS cromosomi "
				+ "FROM genes g "
				+ "WHERE g.Essential = 'Essential' "
				+ "GROUP BY g.GeneID";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			result.add(idMap.get(res.getString("id")));
				}
			
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Arco> getArchi(Map<String,Genes> idMap){
		String sql = "SELECT g1.GeneID AS id1, g2.GeneID AS id2, ABS (i.Expression_Corr) AS peso "
				+ "FROM genes g1, genes g2, interactions i "
				+ "WHERE g1.GeneID <> g2.GeneID AND g1.GeneID = i.GeneID1 AND g2.GeneID = i.GeneID2 "
				+ "GROUP BY g1.GeneID, g2.GeneID "
				+ "ORDER BY peso DESC";
		List<Arco> result = new LinkedList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			Genes g1 = idMap.get(res.getString("id1"));
			Genes g2 = idMap.get(res.getString("id2"));
			if(g1!=null && g2!=null) {
				Arco arco = new Arco(g1,g2,res.getDouble("peso"));
				result.add(arco);
			}

				}
			
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}


	
}
