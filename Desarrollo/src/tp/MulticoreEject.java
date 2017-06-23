package tp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import grafos.GrafoNDNP;

public class MulticoreEject extends Thread{
	
	private String file;
	private GrafoNDNP g;
	private int corridas;
	public MulticoreEject(GrafoNDNP g, String name, int corridas) {
		this.g = g.clone();
		this.file = name;
		this.corridas = corridas;
	}
	
	
	@Override
	public void run() {
		System.out.println("Iniciado "+file);
		try {
			ejecutar();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println("Finalizado "+file);
		Main.finalizo(this);
	}
	
	
	private void ejecutar() throws IOException{
		int rminMatula=0;
		int rminSecuecial=0;
		int rminWP=0;
		// minimos
		int minMatula=0;
		int minSecuecial=0;
		int minWP=0;
		long startTime = System.currentTimeMillis();
		for(int i=0;i<corridas;i++){
			int colores = g.colorearSecuencialAleatorio();
			if(colores<minSecuecial || i==0){
				minSecuecial = colores;
				rminSecuecial = i;
			}
			colores = g.colorearMatula();

			if(colores<minMatula || i==0){
				minMatula = colores;
				rminMatula = i;
			}
			
			colores = g.colorearWP();
			if(colores<minWP || i==0){
				minWP = colores;
				rminWP= i;
			}
			if(i== 100){
				System.out.println("("+file+")Tiempo estimado ("+i+"/"+corridas+"): "+
			((((System.currentTimeMillis() - startTime)/i)*(corridas-i))/60000)+"mins");
			}
		}

		/* informar en consola resultado
		System.out.println("Secuencial aleatorio \n\tMinimo: "+minSecuecial+"\n\tCorrida: "+rminSecuecial);
		System.out.println("Matula \n\tMinimo: "+minMatula+"\n\tCorrida: "+rminMatula);
		System.out.println("Welsh-Powell \n\tMinimo: "+minWP+"\n\tCorrida: "+rminWP);
		*/
		
		int offset = 0;
		String[] sp = file.split("-");
		if(sp.length>1){
			offset = Integer.valueOf(sp[1])*corridas;
		}
		// guarda resultado en archivo
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file)));
		writer.write("Secuencial aleatorio \n\tMinimo: "+minSecuecial+"\n\tCorrida: "+(rminSecuecial+offset));
		writer.newLine();
		writer.write("Matula \n\tMinimo: "+minMatula+"\n\tCorrida: "+(rminMatula+offset));
		writer.newLine();
		writer.write("Welsh-Powell \n\tMinimo: "+minWP+"\n\tCorrida: "+(rminWP+offset));
		writer.close();
	}
}
