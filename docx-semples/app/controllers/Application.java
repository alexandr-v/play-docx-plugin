package controllers;

import play.*;
import play.modules.docx.RenderDocx;
import play.mvc.*;
import play.plugins.PluginCollection;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	
    	request.format = "docx";
    	
    	String testString = "testString";
    	renderArgs.put(RenderDocx.RA_FILENAME, "test.docx");
    	
        render(testString);
    }

}