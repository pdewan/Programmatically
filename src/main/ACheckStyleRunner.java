package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import framework.execution.ProcessRunner;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;

public class ACheckStyleRunner {
	public RunningProject checkStyle(String aSourceFileFlder) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		String xmlContent = getURLContent("http://www.cs.unc.edu/~dewan/comp401/current/Downloads/assignment1/unc_checks_401_f16_a1.xml");
		String username = System.getProperty("user.name");
		File codeFile = null;

		String osName = System.getProperty("os.name");
		String pathname = "";
		Path target = null;
		
		if(osName.contains("Mac")){
			pathname = "/Users/" + username + "/Styles";
			target = Paths.get(pathname);
			pathname = pathname + "/unc_checks_401_f16_a1.xml";
		}else if(osName.contains("Windows")){
			pathname = "C:\\Users\\" + username + "\\Styles";
			pathname = pathname.replace("\\","/");
			target = Paths.get(pathname);
			pathname = pathname + "\\unc_checks_401_f16_a1.xml";
			pathname = pathname.replace("\\","/");
		}else if(osName.contains("Linux")){
			pathname = "/home/" + username + "/Styles";
			target = Paths.get(pathname);
			pathname = pathname + "/unc_checks_401_f16_a1.xml";
		}
		
		codeFile = new File(pathname);
		
		try{
			//this is used to create the file (if it doesn't exist already in that folder)
			if(codeFile.createNewFile()){
				System.err.println("File " + codeFile + " was created!");
			}
			else{
				System.err.println("File " + codeFile + " already exists");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		try {
				//This is writing the string aText into the file
			fw = new FileWriter(codeFile.getAbsoluteFile(), false);
			bw = new BufferedWriter(fw);

			bw.write(xmlContent);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}
		
		String aConfigurationFileName = pathname;

		String windowsName = aSourceFileFlder;

		String[] command = {"java", "com.puppycrawl.tools.checkstyle.Main", "-c",  aConfigurationFileName ,   windowsName };		
		
		String[] args = {};
		Runner processRunner = new ProcessRunner(new File("."));
		//        RunningProject aReturnValue = processRunner.run(null, command, "", args, 2000);
		RunningProject aReturnValue = processRunner.run(null, command, "", args, BasicProjectExecution.PROCESS_TIME_OUT*10);

//		aReturnValue.await();
		return aReturnValue;
	}
	public String getURLContent(String p_sURL){
	    URL oURL;
	    URLConnection oConnection;
	    BufferedReader oReader;
	    String sLine;
	    StringBuilder sbResponse;
	    String sResponse = null;

	    try
	    {
	        oURL = new URL(p_sURL);
	        oConnection = oURL.openConnection();
	        oReader = new BufferedReader(new InputStreamReader(oConnection.getInputStream()));
	        sbResponse = new StringBuilder();

	        while((sLine = oReader.readLine()) != null)
	        {
	            sbResponse.append(sLine);
	        }

	        sResponse = sbResponse.toString();
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }

	    return sResponse;
	}
}

