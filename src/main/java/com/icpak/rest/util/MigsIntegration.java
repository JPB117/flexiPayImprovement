package com.icpak.rest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.icpak.rest.IDUtils;

public class MigsIntegration {
	String payClientIP = "192.168.43.53";
	int payClientPort = 9050;
	private String cmdResponse = "";
	private String exception = "";
	Logger logger = Logger.getLogger(MigsIntegration.class);

	public void setUpConnection() throws UnknownHostException, IOException {
		// Step 1 - Connect to the Payment Client
		// **************************************
		// Create a socket object to the Payment Client
		Socket sock = new Socket(payClientIP, payClientPort);
		// The Buffered Reader data input stream from the socket
		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		// The output stream to the socket
		PrintStream out = new PrintStream(sock.getOutputStream());

		// Step 2 - Test the Payment Client object was created successfully
		// ****************************************************************
		// Test the communication to the Payment Client using an "echo"
		out.print("1,Test\n");
		cmdResponse = in.readLine();
		if (!cmdResponse.equals("1,echo:Test")) {
			logger.error("Failed. Did receive the test response.");
			// perform error handling for this command
			sock.close();
			return;
		} else {
			logger.info("Echo Test successfull.");
		}

		String cardNumber = "5123456789012346";
		String cardExpiry = "0504";
		String merchTxnRef = IDUtils.generateId();
		String OrderInfo = "TESTORDER1";
		String MerchantId = "00000147";
		String PurchaseAmountInteger = "100";

		// Step 3 - Prepare to Generate and Send Digital Order
		// ***************************************************
		// The extra details are not accepted by the
		// sendMOTODigitalOrder() method and so we must add them
		// explicitly here for inclusion in the DO creation.
		// Adding the supplementary data to the DO is done using the
		// "addDigitalOrderField" command
		// add card number
		out.print("7,CardNum," + cardNumber + "\n");
		logger.info("7,CardNum," + cardNumber + "\n");
		cmdResponse = in.readLine();
		// check the status to make sure the command completed OK
		if (!cmdResponse.equals("1,1")) {
			// perform error handling for this command
			logger.error("Failed. Socket refused CardNumber.");
			sock.close();
			return;
		}
		// card expiry date data
		out.print("7,CardExp," + cardExpiry + "\n");
		logger.info("7,CardExp," + cardExpiry + "\n");
		cmdResponse = in.readLine();
		if (!cmdResponse.equals("1,1")) {
			logger.error("Failed. Socket refused CardExp.");
			// perform error handling for this command
			sock.close();
			return;
		}
		// merchant transaction reference data
		out.print("7,MerchTxnRef," + merchTxnRef + "\n");
		logger.info("7,MerchTxnRef," + merchTxnRef + "\n");
		cmdResponse = in.readLine();
		if (!cmdResponse.equals("1,1")) {
			logger.error("Failed. Socket refused MerchTxnRef.");
			// perform error handling for this command
			sock.close();
			return;
		}

		// Step 4 - Generate and Send Digital Order (& receive DR)
		// *******************************************************
		// Create and send the Digital Order
		// (This primary command also receives the Digital Receipt)
		out.print("6," + OrderInfo + "," + MerchantId + "," + PurchaseAmountInteger + ",en,\n");
		logger.info("6," + OrderInfo + "," + MerchantId + "," + PurchaseAmountInteger + ",en,\n");
		cmdResponse = in.readLine();

		if (!cmdResponse.equals("1,1")) {
			// Retrieve the Payment Client Error (There may be none to get)
			out.print("4,PaymentClient.Error\n");
			cmdResponse = in.readLine();
			// check the first status char of the return message
			// if OK there is value to get
			if (cmdResponse.substring(0, 1).equals("1")) {
				exception = cmdResponse.substring(2);
			}
			// perform error handling for this command
			logger.error("Exception:::" + exception);
			sock.close();
			return;
		}

		// Step 5 - Check the DR to see if there is a valid result
		// *******************************************************
		// Use the "nextResult" command to check if the DR contains a result
		out.print("5\n");
		cmdResponse = in.readLine();
		if (!cmdResponse.equals("1,1")) {
			// retrieve the error description from the Payment Client
			out.print("4,PaymentClient.Error\n");
			cmdResponse = in.readLine();
			// check the first status char of the return message
			// if OK there is value to get
			if (cmdResponse.substring(0, 1).equals("1")) {
				exception = cmdResponse.substring(2);
			}
			// perform error handling for this command
			logger.error("Exception:::" + exception);
			sock.close();
			return;
		}
		// Step 6 - Get the result data from the Digital Receipt
		// *****************************************************
		// Also Extract the other available Digital Receipt fields
		// The example retrieves the MerchantId and Amount values,
		// which should be the same as the values sent. In a production
		// system the sent values and the receive values could be checked to
		// make sure they are the same.
		String qsiResponseCode = "";
		// Get the QSI Response Code for the transaction
		out.print("4,DigitalReceipt.QSIResponseCode");
		cmdResponse = in.readLine();
		if (!cmdResponse.substring(0, 1).equals("1")) {
			// Display an Error Page as the QSIResponseCode could not be
			// retrieved
			// perform error handling for this command
			sock.close();
			return;
		} else {
			qsiResponseCode = cmdResponse.substring(2);
		}
		// If non-zero result, check if the result is an error message
		if (!qsiResponseCode.equals("0")) {
			out.print("4,DigitalReceipt.ERROR");
			cmdResponse = in.readLine();
			// check the first status char of the return message
			// if OK there is value to get
			if (!cmdResponse.substring(0, 1).equals("1")) {
				exception = cmdResponse.substring(2);
				// The response is an error message so display an Error Page
				// perform error handling for this command
				sock.close();
				return;
			}
		}

		/* Response Fields */
		String orderInfo = "";
		String merchantID = "";
		String amount = "";
		String transactionNo = "";
		String receiptNo = "";
		String acqResponseCode = "";
		String authorizeID = "";
		String batchNo = "";
		String cardType = "";

		out.print("4,DigitalReceipt.MerchTxnRef\n");
		cmdResponse = in.readLine();
		merchTxnRef = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.OrderInfo\n");
		cmdResponse = in.readLine();
		orderInfo = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.MerchantId\n");
		cmdResponse = in.readLine();
		merchantID = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.PurchaseAmountInteger\n");
		cmdResponse = in.readLine();
		amount = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.TransactionNo\n");
		cmdResponse = in.readLine();
		transactionNo = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.ReceiptNo\n");
		cmdResponse = in.readLine();
		receiptNo = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.AcqResponseCode\n");
		cmdResponse = in.readLine();
		acqResponseCode = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.AuthorizeId\n");
		cmdResponse = in.readLine();
		authorizeID = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.BatchNo\n");
		cmdResponse = in.readLine();
		batchNo = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		out.print("4,DigitalReceipt.CardType\n");
		cmdResponse = in.readLine();
		cardType = cmdResponse.substring(0, 1).equals("1") ? cmdResponse.substring(2) : "Unknown";
		// Step 7 - We are finished with the Payment Client so tidy up
		// ***********************************************************
		sock.close();
	}

	public static void main(String[] args) {
		MigsIntegration it = new MigsIntegration();

		try {
			it.setUpConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
