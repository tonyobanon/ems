
package com.kylantis.eaa.core.pdf.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import com.kylantis.eaa.core.forms.SignatureSpec;

/**
 * A utility for signing a PDF with bouncy castle. A keystore can be created
 * with the java keytool, for example:
 *
 * {@code keytool -genkeypair -storepass 123456 -storetype pkcs12 -alias test -validity 365
 *        -v -keyalg RSA -keystore keystore.p12 }
 *
 */
public class SignatureUtil extends SignatureUtilBase {
	/**
	 * Initialize the signature creator with a keystore and certficate password.
	 *
	 * @param keystore
	 *            the pkcs12 keystore containing the signing certificate
	 * @param pin
	 *            the password for recovering the key
	 * @throws KeyStoreException
	 *             if the keystore has not been initialized (loaded)
	 * @throws NoSuchAlgorithmException
	 *             if the algorithm for recovering the key cannot be found
	 * @throws UnrecoverableKeyException
	 *             if the given password is wrong
	 * @throws CertificateException
	 *             if the certificate is not valid as signing time
	 * @throws IOException
	 *             if no certificate could be found
	 */
	
	private SignatureUtil(KeyStore keystore, char[] pin) throws KeyStoreException, UnrecoverableKeyException,
			NoSuchAlgorithmException, CertificateException, IOException {
		super(keystore, pin);
	}

	public static void signDocument(String inFile, String outFile, SignatureSpec signature) {

		try {

			// load the keystore
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			char[] password = signature.getPassword().toCharArray();
			keystore.load(new FileInputStream(signature.getCertificate()), password);
			
			// TSA client
			TSAClient tsaClient = null;
			if (signature.getTsaUrl() != null) {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				tsaClient = new TSAClient(new URL(signature.getTsaUrl()), null, null, digest);
			}

			// sign PDF
			SignatureUtil signing = new SignatureUtil(keystore, password);
			signing.setExternalSigning(signature.isExternal());
			signing.signDetached(new File(inFile), new File(outFile), tsaClient);

		} catch (IOException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Signs the given PDF file.
	 * 
	 * @param inFile
	 *            input PDF file
	 * @param outFile
	 *            output PDF file
	 * @param tsaClient
	 *            optional TSA client
	 * @throws IOException
	 *             if the input file could not be read
	 */
	private void signDetached(File inFile, File outFile, TSAClient tsaClient) throws IOException {
		if (inFile == null || !inFile.exists()) {
			throw new FileNotFoundException("Document for signing does not exist");
		}

		FileOutputStream fos = new FileOutputStream(outFile);

		// sign
		try (PDDocument doc = PDDocument.load(inFile)) {
			signDetached(doc, fos, tsaClient);
		}
	}

	private void signDetached(PDDocument document, OutputStream output, TSAClient tsaClient) throws IOException {
		setTsaClient(tsaClient);

		int accessPermissions = getMDPPermission(document);
		if (accessPermissions == 1) {
			throw new IllegalStateException(
					"No changes to the document are permitted due to DocMDP transform parameters dictionary");
		}

		// create signature dictionary
		PDSignature signature = new PDSignature();
		signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
		signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
		signature.setName("Example User");
		signature.setLocation("Los Angeles, CA");
		signature.setReason("Testing");
		// TODO extract the above details from the signing certificate? Reason
		// as a parameter?

		// the signing date, needed for valid signature
		signature.setSignDate(Calendar.getInstance());

		// Optional: certify
		if (accessPermissions == 0) {
			setMDPPermission(document, signature, 2);
		}

		if (isExternalSigning()) {
			System.out.println("Sign externally...");
			document.addSignature(signature);
			ExternalSigningSupport externalSigning = document.saveIncrementalForExternalSigning(output);
			// invoke external signature service
			byte[] cmsSignature = sign(externalSigning.getContent());
			// set signature bytes received from the service
			externalSigning.setSignature(cmsSignature);
		} else {
			// register signature dictionary and sign interface
			document.addSignature(signature, this);

			// write incremental (only for signing purpose)
			document.saveIncremental(output);
		}
	}

}