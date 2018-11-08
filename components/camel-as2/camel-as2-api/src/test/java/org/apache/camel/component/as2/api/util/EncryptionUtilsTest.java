package org.apache.camel.component.as2.api.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.component.as2.api.AS2EncryptionAlgorithm;
import org.apache.camel.component.as2.api.Utils;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.junit.Before;
import org.junit.Test;

public class EncryptionUtilsTest {
    
    private static final String PLAIN_TEXT = "This is a super secret!";
    
    private KeyPair issueKP;
    private X509Certificate issueCert;

    private KeyPair signingKP;
    private X509Certificate signingCert;
    private List<X509Certificate> certList;

    @Test
    public void test() throws Exception {
        
        Set<String> sucessfulAlgorithms = new HashSet<String>();
        Map<String,Exception> errors = new HashMap<String,Exception>();
        
        CMSEnvelopedDataGenerator gen = EncryptingUtils.createEnvelopDataGenerator(certList.toArray(new Certificate[0]));
        
        for (AS2EncryptionAlgorithm encryptionAlgorithm: AS2EncryptionAlgorithm.values()) {
            
            try {
                OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(encryptionAlgorithm.getAlgorithmOID()).build();
                CMSTypedData content = new CMSProcessableByteArray(PLAIN_TEXT.getBytes());
                CMSEnvelopedData envelopedData = gen.generate(content, encryptor);
                
                RecipientInformationStore recipientsInformationStore = envelopedData.getRecipientInfos();
                Collection<RecipientInformation> recipients = recipientsInformationStore.getRecipients();
                Iterator<RecipientInformation> it = recipients.iterator();
                
                if (it.hasNext()) {
                    Recipient recipient = new JceKeyTransEnvelopedRecipient(signingKP.getPrivate());
                    RecipientInformation recipientInformation = it.next();
                    recipientInformation.getContent(recipient);
                }

                sucessfulAlgorithms.add(encryptionAlgorithm.name());
            } catch (Exception e) {
                errors.put(encryptionAlgorithm.name(), e);
            }
        }
        
        if (!errors.isEmpty()) {
            throw new Exception("some encryption algorithms failed: " + errors.keySet().toString());
        }
    }

    @Before
    public void setupKeysAndCertificates() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        
        //
        // set up our certificates
        //
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");

        kpg.initialize(1024, new SecureRandom());

        String issueDN = "O=Punkhorn Software, C=US";
        issueKP = kpg.generateKeyPair();
        issueCert = Utils.makeCertificate(issueKP, issueDN, issueKP, issueDN);

        //
        // certificate we sign against
        //
        String signingDN = "CN=William J. Collins, E=punkhornsw@gmail.com, O=Punkhorn Software, C=US";
        signingKP = kpg.generateKeyPair();
        signingCert = Utils.makeCertificate(signingKP, signingDN, issueKP, issueDN);

        certList = new ArrayList<>();

        certList.add(signingCert);
        certList.add(issueCert);

    }

}
