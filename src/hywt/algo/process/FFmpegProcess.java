package hywt.algo.process;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FFmpegProcess {

    private String params;
    private String path;
    private Process ffmpeg;
    private OutputStream pipe;
    private int width;
    private int height;

    public FFmpegProcess(String path, String params) {
        this.path = path;
        this.params = params;
    }

    public FFmpegProcess(String params) {
        this("ffmpeg", params);
    }

    public void writeFrame(BufferedImage frame) throws IOException {
        if (frame == null) throw new NullPointerException("frame is null");
        if (ffmpeg == null) {
            width = frame.getWidth();
            height = frame.getHeight();

            ffmpeg = Subprocess.call(String.format("\"%s\" -r 30 -pix_fmt bgr24 -f rawvideo -s %dx%d -i - %s", path, width, height, params), true);
            pipe = ffmpeg.getOutputStream();

            // stdout重定向
            Runnable r = () -> {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream()));
                while (true) {
                    try {
                        if ((line = br.readLine()) == null) break;
                        System.out.println(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        }
        DataBufferByte buffer = (DataBufferByte) frame.getData().getDataBuffer();
        pipe.write(buffer.getData());
    }
}
