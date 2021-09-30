package algo;

import algo.animation.AnimationGen;
import algo.datatype.VideoSize;
import algo.process.FFmpegProcess;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        create("-c:v libx264 -pix_fmt yuv420p", gen, file);
    }

    public static void x264Lossless(AnimationGen gen, File file) throws IOException {
        create("-c:v libx264 -pix_fmt yuv420p -qp 0", gen, file);
    }

    public static void x265(AnimationGen gen, File file) throws IOException {
        create("-c:v libx265 -pix_fmt yuv420p", gen, file);
    }

    public static void vp9(AnimationGen gen, File file) throws IOException {
        create("-c:v libvpx-vp9 -pix_fmt yuv420p -row-mt 1 -speed 2", gen, file);
    }

    public static void av1(AnimationGen gen, File file) throws IOException {
        create("-c:v libaom-av1 -pix_fmt yuv420p -tiles 2x4 -cpu-used 8", gen, file);
    }

    public static void gif(AnimationGen gen, File file) throws IOException {
        create("-sws_dither a_dither", gen, file);
    }

    public static void nvenc(AnimationGen gen, File file) throws IOException {
        create("-c:v h264_nvenc -pix_fmt yuv420p", gen, file);
    }

    public static void nvenc265(AnimationGen gen, File file) throws IOException {
        create("-c:v hevc_nvenc -pix_fmt yuv420p", gen, file);
    }

    public static void qsv(AnimationGen gen, File file) throws IOException {
        create("-c:v h264_qsv -pix_fmt yuv420p", gen, file);
    }

    public static void qsv265(AnimationGen gen, File file) throws IOException {
        create("-c:v hevc_qsv -pix_fmt yuv420p", gen, file);
    }

    public static void qsvp9(AnimationGen gen, File file) throws IOException {
        create("-c:v vp9_qsv -pix_fmt yuv420p", gen, file);
    }

    public static void amf(AnimationGen gen, File file) throws IOException {
        create("-c:v h264_amf -pix_fmt yuv420p", gen, file);
    }

    public static void amf265(AnimationGen gen, File file) throws IOException {
        create("-c:v hevc_amf -pix_fmt yuv420p", gen, file);
    }

}
