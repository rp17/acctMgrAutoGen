package acctMgr.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CoresNumber {

	public static int getNumberOfCPUCores() {
	    OSValidator osValidator = new OSValidator();
	    String command = "";
	    if(osValidator.isMac()){
	        command = "sysctl -n machdep.cpu.core_count";
	    }else if(osValidator.isUnix()){
	        command = "lscpu";
	    }else if(osValidator.isWindows()){
	        command = "cmd /C WMIC CPU Get /Format:List";
	    }
	    Process process = null;
	    int numberOfCores = 0;
	    int sockets = 0;
	    try {
	        if(osValidator.isMac()){
	            String[] cmd = { "/bin/sh", "-c", command};
	            process = Runtime.getRuntime().exec(cmd);
	        }else{
	            process = Runtime.getRuntime().exec(command);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    BufferedReader reader = new BufferedReader(
	            new InputStreamReader(process.getInputStream()));
	    String line;

	    try {
	        while ((line = reader.readLine()) != null) {
	            if(osValidator.isMac()){
	                numberOfCores = line.length() > 0 ? Integer.parseInt(line) : 0;
	            }else if (osValidator.isUnix()) {
	                if (line.contains("Core(s) per socket:")) {
	                    numberOfCores = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
	                }
	                if(line.contains("Socket(s):")){
	                    sockets = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
	                }
	            } else if (osValidator.isWindows()) {
	                if (line.contains("NumberOfCores")) {
	                    numberOfCores = Integer.parseInt(line.split("=")[1]);
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    if(osValidator.isUnix()){
	        return numberOfCores * sockets;
	    }
	    return numberOfCores;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int logiccores = Runtime.getRuntime().availableProcessors();
		System.out.println("logical cores number = " + logiccores);
		int physcores = (new CoresNumber()).getNumberOfCPUCores();
		System.out.println("physical cores number = " + physcores);
		
	}

}

class OSValidator {

private static String OS = System.getProperty("os.name").toLowerCase();

public static void main(String[] args) {

    System.out.println(OS);

    if (isWindows()) {
        System.out.println("This is Windows");
    } else if (isMac()) {
        System.out.println("This is Mac");
    } else if (isUnix()) {
        System.out.println("This is Unix or Linux");
    } else if (isSolaris()) {
        System.out.println("This is Solaris");
    } else {
        System.out.println("Your OS is not support!!");
    }
}

public static boolean isWindows() {
    return (OS.indexOf("win") >= 0);
}

public static boolean isMac() {
    return (OS.indexOf("mac") >= 0);
}

public static boolean isUnix() {
    return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
}

public static boolean isSolaris() {
    return (OS.indexOf("sunos") >= 0);
}
public static String getOS(){
    if (isWindows()) {
        return "win";
    } else if (isMac()) {
        return "osx";
    } else if (isUnix()) {
        return "uni";
    } else if (isSolaris()) {
        return "sol";
    } else {
        return "err";
    }
}
}

