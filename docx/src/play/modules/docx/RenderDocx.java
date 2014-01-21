package play.modules.docx;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.net.URLCodec;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import play.exceptions.UnexpectedException;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Scope.RenderArgs;
import play.mvc.results.Result;

public class RenderDocx extends Result {
	public static final String RA_FILENAME = "__FILE_NAME__";
    
	private static final URLCodec encoder = new URLCodec();
    
	private File template;
	private Map<String, Object> args;
	
	public RenderDocx(File template, Map<String, Object> args) {
		this.template = template;
		this.args = args;		
	}

	@Override
	public void apply(Request request, Response response) {
		

	        RenderArgs renderArgs = RenderArgs.current();
	        if (!response.headers.containsKey("Content-Disposition")) {
	            String fileName = renderArgs.get(RenderDocx.RA_FILENAME, String.class);
	            
	            if (fileName == null) {
	                response.setHeader("Content-Disposition", "attachment; filename=export." + request.format);
	            } else {
	                try {
	                    response.setHeader(
	                            "Content-Disposition",
	                            "attachment; filename="
	                                    + encoder.encode(fileName, "utf-8"));
	                } catch (UnsupportedEncodingException e) {
	                    throw new UnexpectedException(e);
	                }
	            }

	            response.setContentTypeIfNotSet("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	            
	        }

	        

	        WordprocessingMLPackage wordMLPackage;
			try {
				
				wordMLPackage = WordprocessingMLPackage.load(template);
				MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
				HashMap<String, String> mappings = new HashMap<>();
		        
		        for(String key : args.keySet()){
		        	mappings.put(key, args.get(key).toString());
		        }
		        
		        documentPart.variableReplace(mappings);
		        
		        SaveToZipFile saver = new SaveToZipFile(wordMLPackage);
		        
		        saver.save(new File("./test.docx"));
		        saver.save(response.out);
		        
			} catch (Docx4JException | JAXBException e) {
				e.printStackTrace();
			}
	        	     
	        
	    
	}

}
