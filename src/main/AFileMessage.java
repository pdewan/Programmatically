package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import framework.execution.ProcessRunner;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.Runner;
import grader.basics.execution.RunningProject;
import hermes.messagebus.client.library.Client;
import hermes.messagebus.client.library.MessageReceiver;


public class AFileMessage implements MessageReceiver{
	
	Client clientThread;
	private static final String TAGS = ".*";

	public static void main(String[] args) {
		new AFileMessage().go();
//		String code = "/* package whatever; // don't place package name! */\n\nimport java.util.*;\nimport java.lang.*;\nimport java.io.*;\n\n/* Name of the class has to be \"Main\" only if the class is public. */\nclass Ideone\n{\n    public static void main (String[] args) throws java.lang.Exception\n    {\n        // your code goes here\n    }\n}\n";
//		String code = "//*******************************************************************\n// Dear CompileJava users,\n//\n// CompileJava has been operating since 2013 completely free. If you\n// find this site useful, or would otherwise like to contribute, then\n// please consider a donation (link in 'More Info' tab) to support\n// development of the new CompileJava website (stay tuned!).\n//\n// Most sincerely, Z.\n//*******************************************************************\n \nimport java.lang.Math; // headers MUST be above the first class\n \n// one class needs to have a main() method\npublic class HelloWorld\n{\n  // arguments are passed using the text field below this editor\n  public static void main(String[] args)\n  {\n    OtherClass myObject = new OtherClass(\"Hello World!\");\n    System.out.print(myObject);\n  }\n}\n \n// you can add other public classes to this editor in any order\npublic class OtherClass\n{\n  private String message;\n  private boolean answer = false;\n  public OtherClass(String input)\n  {\n    message = \"Why, \" + input + \" Isn't this something?\";\n  }\n  public String toString()\n  {\n    return message;\n  }\n}\n";
//		receiveMessage("Ram", code);
//		publicClassChecker(code);
//		packageChecker(code);

	}
	
	private void go() {
	    clientThread = new Client(TAGS, this);
	}
	

	public void receiveMessage(String message) throws JSONException{
		JSONObject msgObj = new JSONObject(message);
		String from = msgObj.getString("from");
		String editorContents = msgObj.getString("editorContents");
		
		String styleCheck = receiveMessage(from, editorContents);
		
		JSONObject messageToSend = new JSONObject();
		messageToSend.put("to", from);
		messageToSend.put("result", styleCheck);
		clientThread.sendMessageToUser(messageToSend.toString());
	}

	public static String receiveMessage(String aUserName, String aText){
		BufferedWriter bw = null;
		FileWriter fw = null;
		String osName = System.getProperty("os.name");
		String username = System.getProperty("user.name");
		String pathname = "";
		RunningProject runningProject = null;
		String feedback;
		
		ArrayList<String> className = publicClassChecker(aText);
		ArrayList<String> packageName = packageChecker(aText);
		ArrayList<File> filepathName = new ArrayList<File>();
		
		if(osName.contains("Mac")){
			//Creating the folder with the name given by the string, aUserName
			pathname = "/Users/" + username + "/Styles";
			File folder = new File(pathname);

			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			pathname = "/Users/" + username + "/Styles/" + aUserName;
			folder = new File(pathname);
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			pathname = pathname + "/src";
			folder = new File(pathname);
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			for(int i = 0; i < packageName.size(); i++){
				String namePackage = packageName.get(i);
				pathname = pathname + "/" + namePackage;
				folder = new File(pathname);
				
				try{
					if(folder.exists()){
						System.err.println("Folder " + folder.getName() + " already exists");
					}
					else{
						folder.mkdir();
						System.err.println("Folder " + folder.getName() + " was created!");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			if(packageName.size() == 0){
				pathname = pathname + "/default";
				folder = new File(pathname);
				
				try{
					if(folder.exists()){
						System.err.println("Folder " + folder.getName() + " already exists");
					}
					else{
						folder.mkdir();
						System.err.println("Folder " + folder.getName() + " was created!");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < className.size(); i++){
				String filepath = pathname + "/" + className.get(i) + ".java";
				File codeFile = new File(pathname);
				filepathName.add(codeFile);
			}

			try{
				for(int i = 0; i < className.size(); i++){
					//this is used to create the file (if it doesn't exist already in that folder)
					if(filepathName.get(i).createNewFile()){
						System.err.println("File " + filepathName.get(i).getName() + " was created!");
					}
					else{
						System.err.println("File " + filepathName.get(i).getName() + " already exists");
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			try {
				for(int i = 0; i < className.size(); i++){
					//This is writing the string aText into the file
					fw = new FileWriter(filepathName.get(i).getAbsoluteFile(), false);
					bw = new BufferedWriter(fw);
	
					bw.write(aText);
				}

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
			
			ACheckStyleRunner styleChecker = new ACheckStyleRunner();
			runningProject = styleChecker.checkStyle(pathname);

			
		}else if(osName.contains("Windows")){
			pathname = "C:\\Users\\" + username + "\\Styles";
			pathname = pathname.replace("\\","/");
			File folder = new File(pathname);
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			pathname = "C:\\Users\\" + username + "\\Styles\\" + aUserName;
			pathname = pathname.replace("\\","/");
			folder = new File(pathname);
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			pathname = pathname + "\\src";
			pathname = pathname.replace("\\","/");
			folder = new File(pathname);
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			for(int i = 0; i < packageName.size(); i++){
				String namePackage = packageName.get(i);
				pathname = pathname + "\\" + namePackage;
				pathname = pathname.replace("\\","/");
				folder = new File(pathname);
				
				try{
					if(folder.exists()){
						System.err.println("Folder " + folder.getName() + " already exists");
					}
					else{
						folder.mkdir();
						System.err.println("Folder " + folder.getName() + " was created!");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			if(packageName.size() == 0){
				pathname = pathname + "\\default";
				pathname = pathname.replace("\\","/");
				folder = new File(pathname);
				
				try{
					if(folder.exists()){
						System.err.println("Folder " + folder.getName() + " already exists");
					}
					else{
						folder.mkdir();
						System.err.println("Folder " + folder.getName() + " was created!");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < className.size(); i++){
				String filePathName = pathname + "\\" + className.get(i) + ".java";
				filePathName = filePathName.replace("\\", "/");
				File codeFile = new File(filePathName);
				filepathName.add(codeFile);
			}

			try{
				for(int i = 0; i < className.size(); i++){
					//this is used to create the file (if it doesn't exist already in that folder)
					if(filepathName.get(i).createNewFile()){
						System.err.println("File " + filepathName.get(i).getName() + " was created!");
					}
					else{
						System.err.println("File " + filepathName.get(i).getName() + " already exists");
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			try {
				for(int i = 0; i < className.size(); i++){
					//This is writing the string aText into the file
					fw = new FileWriter(filepathName.get(i).getAbsoluteFile(), false);
					bw = new BufferedWriter(fw);
	
					bw.write(aText);
				}

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
			
			ACheckStyleRunner styleChecker = new ACheckStyleRunner();
			runningProject = styleChecker.checkStyle(pathname);

		}else if(osName.contains("Linux")){
			pathname = "/home/" + username + "/Styles";
			File folder = new File(pathname);
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			pathname = pathname + "/" + aUserName;
			folder = new File(pathname);
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			pathname = pathname + "/src";
			
			try{
				if(folder.exists()){
					System.err.println("Folder " + folder.getName() + " already exists");
				}
				else{
					folder.mkdir();
					System.err.println("Folder " + folder.getName() + " was created!");
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			for(int i = 0; i < packageName.size(); i++){
				String namePackage = packageName.get(i);
				pathname = pathname + "/" + namePackage;
				folder = new File(pathname);
				
				try{
					if(folder.exists()){
						System.err.println("Folder " + folder.getName() + " already exists");
					}
					else{
						folder.mkdir();
						System.err.println("Folder " + folder.getName() + " was created!");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			if(packageName.size() == 0){
				pathname = pathname + "/default";
				folder = new File(pathname);
				
				try{
					if(folder.exists()){
						System.err.println("Folder " + folder.getName() + " already exists");
					}
					else{
						folder.mkdir();
						System.err.println("Folder " + folder.getName() + " was created!");
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < className.size(); i++){
				String filepath = pathname + "/" + className.get(i) + ".java";
				File codeFile = new File(pathname);
				filepathName.add(codeFile);
			}

			try{
				for(int i = 0; i < className.size(); i++){
					//this is used to create the file (if it doesn't exist already in that folder)
					if(filepathName.get(i).createNewFile()){
						System.err.println("File " + filepathName.get(i).getName() + " was created!");
					}
					else{
						System.err.println("File " + filepathName.get(i).getName() + " already exists");
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			try {
				for(int i = 0; i < className.size(); i++){
					//This is writing the string aText into the file
					fw = new FileWriter(filepathName.get(i).getAbsoluteFile(), false);
					bw = new BufferedWriter(fw);
	
					bw.write(aText);
				}

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
			
			ACheckStyleRunner styleChecker = new ACheckStyleRunner();
			runningProject = styleChecker.checkStyle(pathname);

		}
		
		feedback = runningProject.getOutput();
//		System.err.print(feedback);
		
		return feedback;
	}
	
	public static ArrayList publicClassChecker(String aText){
		ArrayList<String> className = new ArrayList<String>();
		int numPublicClasses = 0;
		String nameClass = "";
		
		for(int i = 12; i < aText.length(); i++){
			String name = aText.substring(i - 12, i);
			String comment = aText.substring(i-12, i-10);
			int k = i - 12;
			if(comment.equals("//")){
				while(aText.charAt(k) != '\n'){
					k++;
					comment = aText.substring(k, k+1);
				}
			}
			if(comment.equals("/*")){
				while(!comment.equals("*/")){
					k++;
					comment = aText.substring(k, k+2);
				}
			}
			i = k + 12;
			if(name.equals("public class")){
				numPublicClasses++;
				int j = i + 1;
				while(aText.charAt(j) != '{' && aText.charAt(j) != ' ' && aText.charAt(j) != '\n'){
					j++;
				}
				nameClass = aText.substring(i + 1, j);
				className.add(nameClass);
				i = j + 1;
				break;
			}
		}
		if(className.size() == 0){
			for(int i = 5; i < aText.length(); i++){
				String name = aText.substring(i - 5, i);
				String comment = aText.substring(i-5, i-3);
				int k = i - 5;
				if(comment.equals("//")){
					while(aText.charAt(k) != '\n'){
						k++;
					}
				}
				if(comment.equals("/*")){
					while(!comment.equals("*/")){
						k++;
						comment = aText.substring(k, k+2);
					}
				}
				i = k + 5;
				if(name.equals("class")){
					numPublicClasses++;
					int j = i + 1;
					while(aText.charAt(j) != '{' && aText.charAt(j) != ' ' && aText.charAt(j) != '\n'){
						j++;
					}
					nameClass = aText.substring(i + 1, j);
					className.add(nameClass);
					i = j + 1;
					break;
				}
			}
		}
		return className;
	}

//	static ArrayList classSeparator(String aUserName, String aText){
//		ArrayList<String> codeText = new ArrayList<String>();
//		ArrayList<RunningProject> codeFeedback = new ArrayList<RunningProject>();
//		String[] textCode = aText.split("(?=public class)");
//		int numSplits = textCode.length;
//		for(int i = 0; i < numSplits; i++){
//			codeText.add(textCode[i]);
//		}
//		for(int i = 1; i < numSplits; i++){
//			String text = textCode[0] + textCode[i];
//			RunningProject feedBack = receiveMessage(aUserName, text);
//			codeFeedback.add(feedBack);
//		}
//		return codeFeedback;		
//	}
	
	public static ArrayList packageChecker(String aText){
		ArrayList<String> packageName = new ArrayList<String>();
		String namePackage = "";
		for(int i = 7; i < aText.length(); i++){
			String name = aText.substring(i - 7, i);
			String comment = aText.substring(i-7, i-5);
			int k = i - 7;
			if(comment.equals("//")){
				while(aText.charAt(k) != '\n'){
					k++;
				}
			}
			if(comment.equals("/*")){
				while(!comment.equals("*/")){
					k++;
					comment = aText.substring(k, k+2);
				}
			}
			i = k + 7;
			if(name.equals("package")){
				int j = i + 1;
				while(aText.charAt(j) != ';'){
					if(aText.charAt(j) == '.'){
						namePackage = aText.substring(i + 1, j);
						packageName.add(namePackage);
						i = j;
						j++;
					}
					else{
						j++;
					}
				}
				namePackage = aText.substring(i + 1, j);
				packageName.add(namePackage);
				i = j + 1;
			}
		}
		return packageName;
	}

}


