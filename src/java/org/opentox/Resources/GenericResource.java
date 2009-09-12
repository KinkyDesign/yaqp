package org.opentox.Resources;


import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;



/**
 * REST Web Service
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.1 
 * @deprecated 
 */

@Deprecated
public class GenericResource extends Resource{

    private static final long serialVersionUID = 10012190002208L;

	protected Logger logger;
	protected String htmlHEAD="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""+
                    "\"http://www.w3.org/TR/html4/loose.dtd\">" +
                    "<html><head><title>Chung - RESTful Web Services</title></head><body>";

        protected static final  String
                htmlEND="</body></html>",
                xmlIntro="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
                PMMLIntro="<PMML version=\"3.2\" " +
                " xmlns=\"http://www.dmg.org/PMML-3_2\"  " +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " +
                " <Header copyright=\"DMG.org\" />";

        private static final String
                baseDir="/home/chung/Documents/RESTfulWebServices",
                  uploadDir=baseDir+"/uploads",
                     CLS_uploadDir=uploadDir+"/classification",
                       CLS_textDir=CLS_uploadDir+"/text",
                       CLS_arffDir=CLS_uploadDir+"/arff",
                       CLS_molDir=CLS_uploadDir+"/mols",
                       CLS_dsdDir=CLS_uploadDir+"/dsd",
                       CLS_rangeDir=CLS_uploadDir+"/range",
                       CLS_scaledDir=CLS_uploadDir+"/scaled",
                     REG_uploadDir=uploadDir+"/regression",
                       REG_arffDir=REG_uploadDir+"/arff",
                  modelsDir=baseDir+"/models",
                     CLS_modelsDir=modelsDir+"/classification",
                        CLS_SVM_modelsDir=CLS_modelsDir+"/svm",
                     REG_modelsDir=modelsDir+"/regression",
                        REG_MLR_modelsDir=REG_modelsDir+"/mlr",
                CLS_Tag="OpenTox-",
                REG_Tag="OpenTox-",
                modelTag="Model-";

        public static final String
                port="3000",
                baseURI="http://opentox.ntua.gr:"+port+"/OpenToxServices";




    public GenericResource(Context context, Request request,
            Response response)
    {
        super(context, request, response);
        logger =  Logger.getLogger("org.opentox");
        logger.config(System.getProperty("java.home"));
        logger.config(System.getProperty("java.version"));

     }



    protected String getBaseDirectory(){
        return baseDir;
    }

    /*
     * 1.  Data
     */

    /*
     * 1.1. Classification Data
     */
        protected static String CLS_UploadDir(){
            return CLS_uploadDir;
        }

            protected static String CLS_TextDir(){
                return CLS_textDir;
            }

            protected static String CLS_ArffDir(){
                return CLS_arffDir;
            }

            protected static String CLS_molsDir(){
                return CLS_molDir;
            }

            protected static String CLS_dsdDir(){
                return CLS_dsdDir;
            }

            protected static String CLS_rangeDir(){
                return CLS_rangeDir;
            }

            protected static String CLS_scaledDir(){
                return CLS_scaledDir;
            }


      /*
       * 1.2. Regression Data
       */
            protected static String REG_Dir(){
                return REG_uploadDir;
            }

            protected static String REG_arffDir(){
                return REG_arffDir;
            }



      /*
       * 2. Models
       *
       */

      /*
       * 2.1. Classification Models
       *
       */
            protected static String CLS_SVM_modelsDir(){
                return CLS_SVM_modelsDir;
            }

      /*
       * 2.2. Regression Models
       *
       */
            protected static String REG_MLR_modelsDir(){
                return REG_MLR_modelsDir;
            }


       /*
        * 3. Other
        *
        */
            protected static String CLS_Tag(){
                return CLS_Tag;
            }

            protected static String REG_Tag(){
                return REG_Tag;
            }

            protected static String modelTag(){
                return modelTag;
            }



}