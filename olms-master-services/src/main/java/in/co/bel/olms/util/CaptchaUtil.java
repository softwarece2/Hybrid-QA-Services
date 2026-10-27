package in.co.bel.olms.util;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;

/**
 * Utility Class to generate the captcha
 *
 * @author  Awadhesh Verma
 * @since   24-08-2023
 */

public class CaptchaUtil {

    Logger logger = LoggerFactory.getLogger(CaptchaUtil.class);

    //Creating Captcha Object
    public static Captcha createCaptcha(Integer width, Integer height) {

//        return new Captcha.Builder(width, height)
//                .addBackground(new GradiatedBackgroundProducer())
//                .addText(new DefaultTextProducer(), new DefaultWordRenderer())
//                .gimp(new FishEyeGimpyRenderer())
////                .addNoise(new CurvedLineNoiseProducer())
//                .build();

//        Color backgroundColor = Color.WHITE;
        List<Color> textColors = Arrays.asList(Color.BLUE);

        List<Font> textFonts = Arrays.asList(new Font("Arial", Font.BOLD, 40), new Font("Courier", Font.BOLD, 40));
        char[] codeNumber = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
//                ,'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
//                'W', 'X', 'Y', 'Z'
        };
//        FlatColorBackgroundProducer bgProducer = new FlatColorBackgroundProducer(backgroundColor);
        DefaultWordRenderer wordRenderer = new DefaultWordRenderer(textColors, textFonts);
        return new Captcha.Builder(width, height)
                .addText(new OlmsTextProducer(5, codeNumber), wordRenderer)
                .addBackground(new GradiatedBackgroundProducer())
//                .addBackground(bgProducer)
//                .addBorder()
//                .gimp(new FishEyeGimpyRenderer())
//                .addNoise(new CurvedLineNoiseProducer(Color.BLUE, 3f))
//                .addNoise(new CurvedLineNoiseProducer(Color.RED, 3f))
                .build();

    }

    //Converting to binary String
    public static String encodeCaptcha(Captcha captcha) {
        String image = null;
        try {
            ByteArrayOutputStream bos= new ByteArrayOutputStream();
            ImageIO.write(captcha.getImage(),"jpg", bos);
            byte[] byteArray= Base64.getEncoder().encode(bos.toByteArray());
            image = new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}