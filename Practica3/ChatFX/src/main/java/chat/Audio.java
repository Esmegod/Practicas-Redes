package chat;
import javax.sound.sampled.*;
import java.io.*;

public class Audio extends Thread{
    static final long RECORD_TIME = 5000; // 5 seconds
    AudioFormat audioFormat;
    File wavFile;
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	TargetDataLine line;
	AudioInputStream ais = null;
    
    public Audio(String nombreArchivo){
        wavFile = new File(nombreArchivo);
    }

	AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;// muestras x segundo (ya sea 1 o 2 canales. 8000 muestras x canal)
		int sampleSizeInBits = 16; // #bits usados para almacenar c/muestra (8 o 16 bits valores típicos)
		int channels = 2; // 1=mono, 2=stereo
		boolean signed = true; // indica si los datos de la muestra van con signo/sin signo
		boolean bigEndian = false; // indica el orden de bits(0=little-endian, 1=Big-endian)//importante x tam muestra(1 o 2 bytes)
		/* construye un formato de audio con codificación lineal PCM() con el tamaño de
		 * trama especificado al # de bits requeridos para una muestra x canal*/
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);// codificación, // PCM(modulación, por pulsos, codificados)
		return format;
	}

    

    @Override
    public void run(){
		try {
			int micro = 3;
            this.start(micro);
		} catch (Exception e) {
			e.printStackTrace();
		} // catch
    }

    void start(int m) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			Mixer mixer = AudioSystem.getMixer(mixerInfo[m]);// 3,5
			
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			} 

			line = (TargetDataLine) mixer.getLine(info);
			line.open(format);
			line.start(); 
			System.out.println("Start capturing...");
		    ais = new AudioInputStream(line);
			System.out.println("Start recording...");
			AudioSystem.write(ais, fileType, wavFile);
			br.close();
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

    void finish() {
		try {
			line.stop();
			line.close();
			ais.close();
			System.out.println("Finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
