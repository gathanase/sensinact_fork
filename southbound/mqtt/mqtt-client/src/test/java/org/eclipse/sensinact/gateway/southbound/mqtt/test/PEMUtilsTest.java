/**
 * Copyright (c) 2012 - 2024 Data In Motion and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.eclipse.sensinact.gateway.southbound.mqtt.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import org.eclipse.sensinact.gateway.southbound.mqtt.impl.PEMUtils;
import org.junit.jupiter.api.Test;

/**
 * Test the PEM utils class
 */
class PEMUtilsTest {

    /**
     * the KEY_EC is generated by:
     *
     * <code><br>
     * openssl ecparam -name prime256v1 -genkey --noout --out test-ec.key<br>
     * openssl pkcs8 -topk8 -nocrypt -in test-ec.key  -out test-ec-pkcs8.key
     * </code>
     */
    private static final String KEY_EC = "-----BEGIN PRIVATE KEY-----\n"
            + "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgYbReCMZ6vzDtif1n\n"
            + "Lm3H0gkLAQf2nD4st59J84A3VsehRANCAAQeCeviOND1dj3dyiVkTYBoaDN55HAF\n"
            + "jBOaPam+Nvw9c9ewndviuXuJ8i/mrxUVjyZeVX2qHZlXxywym+BbzpBb\n"
            + "-----END PRIVATE KEY-----";

    /**
     * the KEY_RSA is generated by:
     *
     * <code><br>
     * openssl genrsa -out test-rsa.key 2048
     * </code>
     */
    private static final String KEY_RSA = "-----BEGIN PRIVATE KEY-----\n"
            + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCIhsGA0A1wmHLz\n"
            + "dbJUSiudhjHs8Ur+5uJr4jWmzHtd/yACfiY1GRnsRKIBJ7kgP+d7hFCjCIcd8DCp\n"
            + "qdVYtnnc2vNNdbbnJwOIodrFbg72eCI35y5OhvuEVaGsyS+965eqZ+DW/g7c7KCS\n"
            + "+swQH32toDYFnz6RMp/0f6n/2KpICk4bfVnY0/qcoe872lQBZNf43+N2Kw8Iv20Q\n"
            + "KWIgbyu8EiqeVGV21vRt+ald9eDlY1Mznneu/Qs/IgZRzbYMbWcqgLMZS2nZlkGi\n"
            + "OdhwV77Z9XANC1rSuu9NVfpW/Z8zxTpHnDMIvCIaIRyAWXZNGTFo8VWDkgRYMCNn\n"
            + "fLni9YTvAgMBAAECggEAF4oAZ/i3MIAgZaiX0B+fqH/AMOWjdBKT3Fz6uiEMbjb0\n"
            + "KmncpZAH36wvsHpMWWqbOzkjfBCW10sX4NDW535CwZkAlQzkNbOM0OxzH1IsA0PQ\n"
            + "Roo1+jUcvYLUJsnjUOADXM6fPgI8xsuHnf/e2jLLxPlYBx0fOQKdAJdYVLPjHBtm\n"
            + "ReRIJPSt24Aggh4waLadu5VaJx1BmoOkDLw2aPF87cFDMtljDsFoQASdVTWGdcVv\n"
            + "JL6sAN5anj7peXP+s2qrmn6PBjtLQrAAHa/clfaSVJcTF+kwR/ITySdvSrmVh2uv\n"
            + "3eDzL/hcSPHK+scHuzfwGNY87suEVJu2aYJjkwXSuQKBgQC9qR39wCuTrrGktztX\n"
            + "9xABlECiBdwsbaoNXkJA6vbwv64yv05boq9HwHVsIPzN54w/Q2AH1GDq0/Ly9HSr\n"
            + "CLJ5HKZXOb1ZBIwbXpR8RUsnGquo8e6b5oky8JAyKxmXdI1FLURMqJo5yM/Onssc\n"
            + "RBURtrcpV8jhBRpBGDdYGMTb2QKBgQC4R8/stA71Ra9ni8ab6ekH3nO9WxIBAPvr\n"
            + "Ih9tIAodTRvpjNZlLIlMbRBNGe1yqRwOPIfHmWXvIOrR4YLz8QrB5OMxug1hbkz6\n"
            + "N4xXfmKfLLfbxLUudFYsKfNSShI4RpUfiS4WRZcLPBk+dlB0pnWQ4PKHUJpFf4M+\n"
            + "UknsL2hSBwKBgFMlX/ONms0Mqe5XMdn3gvneP5OIVCTaEtH3f74sBAQ5VCoFOlnr\n"
            + "8UHYeuHjeFDgcNiPNftYvQBV2M1wI/GTR4LW74mP05XB65ZYGWp9ilvjUlwna/7G\n"
            + "y2Ecm2qTAI4oV4J7PNUKmUo54fhoHw0OP9pbKuMiC/uyG/droV+qxCrJAoGAFIBQ\n"
            + "vnmtrFXLVM61EsVsGmcCI0/NafUtEZSjQGWvGmFouIvlki3pPGppxNock4QCzgan\n"
            + "6GnFcFwOI7ld8zuewcFwESkssekugSvK3jT+Tc0Qy09QBNIGgDVOj5oT+tTHZFHD\n"
            + "odCuJB7UhCFvg/q825hbAvjRoOe1tyo6dR81EOMCgYEArR9yEJzP/vsT5bpnId/a\n"
            + "64kfu4qQXBZqodYeQ1+WpINMgqYgkx+7pFlFT59XhFCavsEUXfIOMsT4LtWHPCJd\n"
            + "0jQlS8JzaZ1O8VdMvtHkgoLb7StEfwJiyQpSoIMffyjReGxej69vqAgPRRzEKGre\n"
            + "wtCcnUW90Y6sz3rzWIfLUtY=\n"
            + "-----END PRIVATE KEY-----";

    @Test
    void testLoadPrivateKeyRSA() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey privateKey = PEMUtils.loadPrivateKey(KEY_RSA);
        assertEquals("RSA", privateKey.getAlgorithm());
    }

    @Test
    void testLoadPrivateKeyWithAlgorithm() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey privateKey = PEMUtils.loadPrivateKey(KEY_EC, "EC");
        assertEquals("EC", privateKey.getAlgorithm());
    }
}