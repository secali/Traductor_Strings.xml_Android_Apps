import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;



public class Main {
	
	public static void main (String args []) {
		Translate translate = TranslateOptions.getDefaultInstance().getService();	//objeto para traduccion
		boolean nuevoFich = true;	//escribe cabecera de nuevo fichero de salida XML
		
		try (BufferedReader br = new BufferedReader(new FileReader("strings.xml"))) {	//flujo lectura
			PrintWriter out = new PrintWriter("SALIDA.xml");	//flujo escritura
			
			StringBuilder sb = new StringBuilder();
			String linea = br.readLine();
			System.out.println("LEIDO: "+linea);	//si el fichero empieza por la linea <?xml... etc tiene formato valido
			
			if(linea.compareToIgnoreCase("<?xml version=\"1.0\" encoding=\"utf-8\"?>")==0) {
				System.out.println("Se ha encontrado cabecera de XML...");
				
				do {
					linea = br.readLine();
				}while(linea.compareToIgnoreCase("<resources>")!=0);	//busca dónde comienza <resources>
				
				System.out.println("Se ha encontrado el inicio de RESOURCES...");
				System.out.println("--------------------------------------------------------------------------------");
				
				do {								//mientras se encuentre "String name..."
					linea = br.readLine();
					
					if(linea.contains("string name")) {
						
						
						StringBuilder aux = new StringBuilder (linea);
						String packageName = aux.substring(0, aux.indexOf(">"));	//coge nombre package
						
						aux.delete(0, aux.indexOf(">")+1);
						//System.out.println(aTraducir);
						
						//oge la cadena a traducir
						StringBuilder aTraducir = aux.delete(aux.indexOf("</"), aux.indexOf(">")+1);
						System.out.print(aTraducir+"------->");
						
						Translation translation =				//traduce la cadena de ingles a español
						        translate.translate(
						            aTraducir.toString(),
						            TranslateOption.sourceLanguage("en"),
						            TranslateOption.targetLanguage("es"));
						String traduccion = translation.getTranslatedText();
						System.out.println(traduccion);
						
						if (nuevoFich) {			//crea cabecera del fichero de salida
						out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
						out.println("<resources>");
						nuevoFich = false;
						}
						
						out.println(packageName+">"+traduccion+"</string>");	//escribe nombre package+traduccion
						//TRANSCRIBIR A NUEVO XML TRADUCIDO
					}
					
					
					
				}while(linea.compareToIgnoreCase("</resources>")!=0	);	
				System.out.println("Fin del fichero XML");
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				
		out.print("</resources>");	//cierra el fichero de salida y los flujos
		out.close();
		br.close();
				
				
			}else System.out.println("ERROR: No se ha encontrado cabecera!");
		} catch (FileNotFoundException e) {
				System.err.println("NO SE HA ENCONTRADO FICHERO strings.xml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
}
