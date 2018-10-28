import java.io.IOException;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;


public class main {

	private static String convert(String input) {
		//System.out.println(input);
		String output1;
		String output2;
		String output3;
		
		output1 = input.substring(0, 1)+".";
		output2 =input.substring(1, input.length());
		output3 ="E"+(input.length()-1);
		String output = output1+output2+output3;
		return output;
	}
	
	public static void coninterface() {
		boolean running = true;
		EmailCS emailer = new EmailCS();
		excelInput input = new excelInput("C:\\Users\\dogbi\\eclipse-workspace\\AEPChallenge\\Data.xls");
		customer temp = null;
		int determin;
		String line;
		Scanner in = new Scanner(System.in);
		while(running) {
			System.out.println("enter a number to pick the corresponding option");
			System.out.println("0: choose a email to send to");
			System.out.println("1: choose a person");
			System.out.println("2: generate a email");
			System.out.println("3: input new data");
			System.out.println("4: quit");
			determin=in.nextInt();
			switch (determin) {
			case 0:
				System.out.println("input a email to send to : example@internet.net");
				in.nextLine();
				emailer.setTo(in.nextLine());
				break;
			case 1:
				System.out.println("input a person to find via customer  key : example 13095811e7");
				in.nextLine();
				String ltemp = in.nextLine();
				//System.out.println(convert(ltemp));
				temp= input.createOneCustomor(convert(ltemp));
				break;
			case 2:
				System.out.println("generating email");
				in.nextLine();
				if(temp ==null) {
					System.out.println("enter a person to find first");
					line = in.nextLine();
					temp= input.createOneCustomor(convert(line));
				}
				try {
					emailer.sendMessage3(temp);
				} catch (MessagingException e) {
					
					e.printStackTrace();
				}
				break;
			case 3:
				System.out.println("input a filepath for data file");
				in.nextLine();
				line = in.nextLine();
				input.setFILE_PATH(line);
				break;
			case 4:
				running =false;
				break;
			}
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		coninterface();
	
	}

}
