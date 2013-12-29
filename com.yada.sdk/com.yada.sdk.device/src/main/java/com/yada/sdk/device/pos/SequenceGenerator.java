package com.yada.sdk.device.pos;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SequenceGenerator {
	MappedByteBuffer byteBuffer;

	public SequenceGenerator(String key) {
		Path path = Paths.get(System.getProperty("user.dir")).resolve(
				"Sequence");
		File dir = path.toFile();

		if (!dir.exists())
			dir.mkdir();

		path = path.resolve(key + ".seq");

		try (FileChannel fc = FileChannel.open(path, StandardOpenOption.CREATE,
				StandardOpenOption.WRITE, StandardOpenOption.READ);) {

			if (fc.size() == 0) {
				ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
				buffer.putInt(0);
				buffer.flip();
				fc.write(buffer);
			}

			byteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0,
					Integer.SIZE / 8);
		} catch (IOException e) {
		}

	}

	public synchronized int getSequence() {
		byteBuffer.position(0);
		int ret = byteBuffer.getInt();
		byteBuffer.position(0);

		ret = ret > 999999 ? 0 : ret;
		byteBuffer.putInt(ret + 1);
		return ret;
	}
}
