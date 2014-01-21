package play.modules.docx;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

import play.PlayPlugin;
import play.exceptions.UnexpectedException;
import play.templates.Template;
import play.vfs.VirtualFile;

public class DocxPlugin extends PlayPlugin {

	private final static Pattern p_ = Pattern.compile(".*\\.(docx)");

	@Override
	public Template loadTemplate(VirtualFile file) {
		if (!p_.matcher(file.getName()).matches()) return null;
        return new DocxTemplate(file);
        
	}
	
	
	public static class DocxTemplate extends Template {
        
        private File file = null;
        private RenderDocx r_ = null;
        
        public DocxTemplate(VirtualFile file) {
            this.name = file.relativePath();
            this.file = file.getRealFile();
        }
        
        public DocxTemplate(RenderDocx render) {
            r_ = render;
        }

        @Override
        public void compile() {
            if (!file.canRead()) throw new UnexpectedException("template file not readable: " + name);
        }

        @Override
        protected String internalRender(Map<String, Object> args) {
            throw null == r_ ? new RenderDocx(file, args) : r_;
        }
    }

}
