/*
 * Copyright 2022 Neil Madden.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package software.pando.crypto.nacl;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.Digests.fromHex;

public class HKDFTest {

    @DataProvider
    public Object[][] rfc5869TestVectors() {
        return new Object[][] {
                {
                    "SHA-256", // Hash
                    "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", // IKM
                    "000102030405060708090a0b0c", // Salt
                    "f0f1f2f3f4f5f6f7f8f9", // Info
                    42, // L
                    "077709362c2e32df0ddc3f0dc47bba6390b6c73bb50f9c3122ec844ad7c2b3e5", // PRK
                    "3cb25f25faacd57a90434f64d0362f2a2d2d0a90cf1a5a4c5db02d56ecc4c5bf34007208d5b887185865" // OKM
                },
                {
                    "SHA-256",
                    "000102030405060708090a0b0c0d0e0f" +
                    "101112131415161718191a1b1c1d1e1f" +
                    "202122232425262728292a2b2c2d2e2f" +
                    "303132333435363738393a3b3c3d3e3f" +
                    "404142434445464748494a4b4c4d4e4f",
                    "606162636465666768696a6b6c6d6e6f" +
                    "707172737475767778797a7b7c7d7e7f" +
                    "808182838485868788898a8b8c8d8e8f" +
                    "909192939495969798999a9b9c9d9e9f" +
                    "a0a1a2a3a4a5a6a7a8a9aaabacadaeaf",
                    "b0b1b2b3b4b5b6b7b8b9babbbcbdbebf" +
                    "c0c1c2c3c4c5c6c7c8c9cacbcccdcecf" +
                    "d0d1d2d3d4d5d6d7d8d9dadbdcdddedf" +
                    "e0e1e2e3e4e5e6e7e8e9eaebecedeeef" +
                    "f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff",
                    82,
                    "06a6b88c5853361a06104c9ceb35b45c" +
                    "ef760014904671014a193f40c15fc244",
                    "b11e398dc80327a1c8e7f78c596a4934" +
                    "4f012eda2d4efad8a050cc4c19afa97c" +
                    "59045a99cac7827271cb41c65e590e09" +
                    "da3275600c2f09b8367793a9aca3db71" +
                    "cc30c58179ec3e87c14c01d5c1f3434f" +
                    "1d87"
                },
                {
                    "SHA-256",
                    "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b",
                    "",
                    "",
                    42,
                    "19ef24a32c717b167f33a91d6f648bdf" +
                    "96596776afdb6377ac434c1c293ccb04",
                    "8da4e775a563c18f715f802a063c5a31" +
                    "b8a11f5c5ee1879ec3454e5f3c738d2d" +
                    "9d201395faa4b61a96c8"
                },
                {
                    "SHA-1",
                    "0b0b0b0b0b0b0b0b0b0b0b",
                    "000102030405060708090a0b0c",
                    "f0f1f2f3f4f5f6f7f8f9",
                    42,
                    "9b6c18c432a7bf8f0e71c8eb88f4b30baa2ba243",
                    "085a01ea1b10f36933068b56efa5ad81" +
                    "a4f14b822f5b091568a9cdd4f155fda2" +
                    "c22e422478d305f3f896"
                },
                {
                    "SHA-1",
                    "000102030405060708090a0b0c0d0e0f" +
                    "101112131415161718191a1b1c1d1e1f" +
                    "202122232425262728292a2b2c2d2e2f" +
                    "303132333435363738393a3b3c3d3e3f" +
                    "404142434445464748494a4b4c4d4e4f",
                    "606162636465666768696a6b6c6d6e6f" +
                    "707172737475767778797a7b7c7d7e7f" +
                    "808182838485868788898a8b8c8d8e8f" +
                    "909192939495969798999a9b9c9d9e9f" +
                    "a0a1a2a3a4a5a6a7a8a9aaabacadaeaf",
                    "b0b1b2b3b4b5b6b7b8b9babbbcbdbebf" +
                    "c0c1c2c3c4c5c6c7c8c9cacbcccdcecf" +
                    "d0d1d2d3d4d5d6d7d8d9dadbdcdddedf" +
                    "e0e1e2e3e4e5e6e7e8e9eaebecedeeef" +
                    "f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff",
                    82,
                    "8adae09a2a307059478d309b26c4115a224cfaf6",
                    "0bd770a74d1160f7c9f12cd5912a06eb" +
                    "ff6adcae899d92191fe4305673ba2ffe" +
                    "8fa3f1a4e5ad79f3f334b3b202b2173c" +
                    "486ea37ce3d397ed034c7f9dfeb15c5e" +
                    "927336d0441f4c4300e2cff0d0900b52" +
                    "d3b4"
                },
                {
                    "SHA-1",
                    "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b",
                    "",
                    "",
                    42,
                    "da8c8a73c7fa77288ec6f5e7c297786aa0d32d01",
                    "0ac1af7002b3d761d1e55298da9d0506" +
                    "b9ae52057220a306e07b6b87e8df21d0" +
                    "ea00033de03984d34918"
                },
                {
                    "SHA-1",
                    "0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c",
                    null,
                    "",
                    42,
                    "2adccada18779e7c2077ad2eb19d3f3e731385dd",
                    "2c91117204d745f3500d636a62f64f0a" +
                    "b3bae548aa53d423b0d1f27ebba6f5e5" +
                    "673a081d70cce7acfc48"
                }
        };
    }

    @Test(dataProvider = "rfc5869TestVectors")
    public void shouldMatchRfc5869TestVectors(String hashAlg, String ikmHex, String saltHex, String infoHex,
            int outputLength, String expectedPrkHex, String outputKeyMaterialHex) {
        // Given
        var hmacAlg = "Hmac" + hashAlg.replace("-", "");
        var hkdf = new HKDF(hmacAlg);

        // When
        var prk = hkdf.extract(saltHex == null ? null : fromHex(saltHex), fromHex(ikmHex));
        var okm = hkdf.expand(prk, fromHex(infoHex), outputLength);

        assertThat(prk.getEncoded()).asHexString().isEqualToIgnoringCase(expectedPrkHex);
        assertThat(okm).asHexString().isEqualToIgnoringCase(outputKeyMaterialHex);
    }
}