﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Serialization.Json;
using System.Security.Cryptography;
using System.Text;

namespace ConsoleApp.BestPractices
{
    public static class SecurityHelper
    {

        public static string ToBase64(this string input)
        {
            var inputBytes = Encoding.UTF8.GetBytes(input);
            var result = Convert.ToBase64String(inputBytes);
            return result;
        }

        public static string FromBase64(this string input)
        {
            var inputBytes = Convert.FromBase64String(input);
            var result = Encoding.UTF8.GetString(inputBytes);
            return result;
        }

        /// <summary>
        /// ToSHA1
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string ToSHA1(this string input)
        {
            using (var provider = new SHA1CryptoServiceProvider())
            {
                var inputBytes = Encoding.UTF8.GetBytes(input);
                var hashbytes = provider.ComputeHash(inputBytes);
                var result = BitConverter.ToString(hashbytes);
                return result.Replace("-", "");
            }
        }

        public static byte[] ToBytes(this string hex)
        {
            var bytes = new byte[hex.Length / 2];
            for (int i = 0; i < hex.Length; i += 2)
            {
                bytes[i / 2] = Convert.ToByte(hex.Substring(i, 2), 16);
            }
            return bytes;
        }

        /// <summary>
        /// 采用SHA512算法对字符串加密
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string ToSHA512(this string input)
        {
            using (var provider = new SHA512Managed())
            {
                var inputBytes = Encoding.UTF8.GetBytes(input);
                var hashbytes = provider.ComputeHash(inputBytes);
                var result = BitConverter.ToString(hashbytes);
                return result.Replace("-", "");
            }
        }

        /// <summary>
        /// 16位，低8位
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string To16bitMD5(this string input)
        {
            return input.ToMD5(true);
        }

        /// <summary>
        /// 32位
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string To32bitMD5(this string input)
        {
            return input.ToMD5(false);
        }

        /// <summary>
        /// MD5
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string ToMD5(this string input, bool is16bit)
        {
            using (var provider = new MD5CryptoServiceProvider())
            {
                var inputbytes = Encoding.UTF8.GetBytes(input);
                var hashbytes = provider.ComputeHash(inputbytes);
                var result = is16bit ? BitConverter.ToString(hashbytes, 4, 8) : BitConverter.ToString(hashbytes);
                return result.Replace("-", "");
            }
        }


    }




    public static class AESHelper
    {
        /// <summary>
        /// 写死的Key及IV
        /// </summary>
        internal static (string Key, string IV) FixedKeys = ("ll7uz0DREVGBA9IJxmnwoEsJoQtgpGPqXQOzmYgaS6o=", "yuntM97GbF5ISjSsx0qKqA==");


        /// <summary>
        /// 实际使用时生成Key及IV后保存起来
        /// </summary>
        /// <returns></returns>
        internal static (string Key, string IV) GetKeys()
        {
            using (var provider = new AesCryptoServiceProvider())
            {
                provider.GenerateKey();
                provider.GenerateIV();

                var key = Convert.ToBase64String(provider.Key);
                var iv = Convert.ToBase64String(provider.IV);

                return (key, iv);
            }
        }


        public static string AESEncrypt(this string input, string key, string iv)
        {
            string result;

            var rgbKey = Convert.FromBase64String(key);
            var rgbIV = Convert.FromBase64String(iv);

            using (var provider = new AesCryptoServiceProvider())
            using (var crypto = provider.CreateEncryptor(rgbKey, rgbIV))
            using (var ms = new MemoryStream())
            using (var cs = new CryptoStream(ms, crypto, CryptoStreamMode.Write))
            {
                using (var sw = new StreamWriter(cs))
                {
                    sw.Write(input);
                }
                result = Convert.ToBase64String(ms.ToArray());
            }

            return result;
        }





        public static string AESDecrypt(this string input, string key, string iv)
        {
            string result;

            var inputBytes = Convert.FromBase64String(input);
            var rgbKey = Convert.FromBase64String(key);
            var rgbIV = Convert.FromBase64String(iv);

            using (var provider = new AesCryptoServiceProvider())
            using (var crypto = provider.CreateDecryptor(rgbKey, rgbIV))
            using (var ms = new MemoryStream(inputBytes))
            using (var cs = new CryptoStream(ms, crypto, CryptoStreamMode.Read))
            {
                using (var read = new StreamReader(cs))
                {
                    result = read.ReadToEnd();
                }
            }

            return result;
        }



    }

    public static class RSAHelper
    {
        /// <summary>
        /// 实际使用时生成一对Key要记录好，PublicKey给调用方，PrivateKey自己保留好
        /// 建议用这个工具生成：https://github.com/yongfa365/RsaKeyGen
        /// </summary>
        /// <returns></returns>
        internal static (string PublicKey, string PrivateKey) GenPublicKeyAndPrivateKey()
        {
            using (var provider = new RSACryptoServiceProvider())
            {
                var publicKey = SerializeHelper.ToXml(provider.ExportParameters(false)); // 公钥
                var privateKey = SerializeHelper.ToXml(provider.ExportParameters(true)); // 私钥
                return (publicKey, privateKey);
            }
        }

        /// <summary>
        /// 用私钥生成公钥
        /// </summary>
        /// <param name="privateKey"></param>
        /// <returns></returns>
        public static string GetPublicKeyByPrivateKey(string privateKey)
        {
            var param = SerializeHelper.FromXml<RSAParameters>(privateKey);
            using (var provider = new RSACryptoServiceProvider())
            {
                provider.ImportParameters(param);
                var publicKey = SerializeHelper.ToXml(provider.ExportParameters(false));
                return publicKey;
            }
        }

        /// <summary>
        /// RSA加密
        /// </summary>
        /// <param name="publicKey"></param>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string RSAEncrypt(this string input, string publicKey)
        {
            var param = SerializeHelper.FromXml<RSAParameters>(publicKey);
            var inputBytes = Encoding.UTF8.GetBytes(input);
            using (var provider = new RSACryptoServiceProvider())
            {
                provider.ImportParameters(param);
                var bytes = provider.Encrypt(inputBytes, false);
                var result = Convert.ToBase64String(bytes);
                return result;
            }
        }


        /// <summary>
        /// RSA解密
        /// </summary>
        /// <param name="privateKey"></param>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string RSADecrypt(this string input, string privateKey)
        {
            var param = SerializeHelper.FromXml<RSAParameters>(privateKey);
            var inputBytes = Convert.FromBase64String(input);
            using (var provider = new RSACryptoServiceProvider())
            {
                provider.ImportParameters(param);
                var bytes = provider.Decrypt(inputBytes, false);
                var result = Encoding.UTF8.GetString(bytes);
                return result;
            }
        }




        //TODO:私钥加密，公钥解密




        public static string RSASingnature(this string srcHash, string privateKey)
        {
            var hashBytes = srcHash.ToBytes();
            var param = SerializeHelper.FromXml<RSAParameters>(privateKey);
            using (var provider = new RSACryptoServiceProvider())
            {
                provider.ImportParameters(param);
                var bytes = provider.SignHash(hashBytes, "SHA1");
                var result = Convert.ToBase64String(bytes);
                return result;
            }
        }


        public static bool RSASingnatureCheck(this string input, string srcHash, string publicKey)
        {
            var hashBytes = srcHash.ToBytes();
            var param = SerializeHelper.FromXml<RSAParameters>(publicKey);
            var inputBytes = Convert.FromBase64String(input);
            using (var provider = new RSACryptoServiceProvider())
            {
                provider.ImportParameters(param);
                var result = provider.VerifyHash(hashBytes, "SHA1", inputBytes);
                return result;
            }
        }
    }
}
