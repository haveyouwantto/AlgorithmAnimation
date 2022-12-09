package hywt.algo;

import hywt.algo.animation.AnimationGen;
import hywt.algo.datatype.VideoSize;
import hywt.algo.process.FFmpegProcess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

public class CreateAnimation {
    public static void create(String params, AnimationGen gen, File file) throws IOException {
        FFmpegProcess ffmpeg = new FFmpegProcess(params + " -y " + file.toString());
        VideoSize size = gen.getSize();
        BufferedImage frameBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) frameBuffer.getGraphics();
        while (gen.hasNext()) {
            gen.provideFrame(graphics);
            ffmpeg.writeFrame(frameBuffer);
        }
    }

    public static void x264(AnimationGen gen, File file) throws IOException {
        create("-c:v libx264 -crf 16 -pix_fmt yuv420p", gen, file);
    }

    public static void x264Lossless(AnimationGen gen, File file) throws IOException {
        create("-c:v libx264 -pix_fmt yuv420p -qp 0", gen, file);
    }

    public static void x265(AnimationGen gen, File file) throws IOException {
        create("-c:v libx265 -crf 18 -pix_fmt yuv420p", gen, file);
    }

    public static void vp9(AnimationGen gen, File file) throws IOException {
        create("-c:v libvpx-vp9 -crf 20 -pix_fmt yuv420p -row-mt 1 -speed 2", gen, file);
    }

    public static void av1(AnimationGen gen, File file) throws IOException {
        create("-c:v libaom-av1 -pix_fmt yuv420p -g 300 -tiles 2x4 -cpu-used 8 -crf 23", gen, file);
    }

    public static void gif(AnimationGen gen, File file) throws IOException {
        create("-sws_dither a_dither", gen, file);
    }

    public static void nvenc(AnimationGen gen, File file) throws IOException {
        create("-c:v h264_nvenc -qp 16 -pix_fmt yuv420p", gen, file);
    }

    public static void nvenc265(AnimationGen gen, File file) throws IOException {
        create("-c:v hevc_nvenc -qp 16 -pix_fmt yuv420p", gen, file);
    }

    public static void qsv(AnimationGen gen, File file) throws IOException {
        create("-c:v h264_qsv -qp 16 -pix_fmt yuv420p", gen, file);
    }

    public static void qsv265(AnimationGen gen, File file) throws IOException {
        create("-c:v hevc_qsv -qp 16 -pix_fmt yuv420p", gen, file);
    }

    public static void qsvp9(AnimationGen gen, File file) throws IOException {
        create("-c:v vp9_qsv -qp 16 -pix_fmt yuv420p", gen, file);
    }

    public static void amf(AnimationGen gen, File file) throws IOException {
        create("-c:v h264_amf -qp 16 -pix_fmt yuv420p", gen, file);
    }

    public static void amf265(AnimationGen gen, File file) throws IOException {
        create("-c:v hevc_amf -qp 16 -pix_fmt yuv420p", gen, file);
    }

    public static void png(AnimationGen gen, File file) throws IOException {
        VideoSize size = gen.getSize();
        BufferedImage frameBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) frameBuffer.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int frame = 0;
        while (gen.hasNext()) {
            gen.provideFrame(graphics);
            System.out.printf("writing frame %d\r", frame);
            ImageIO.write(frameBuffer, "png", new File(file, String.format("%05d.png", frame)));
            frame++;
        }
    }
}
