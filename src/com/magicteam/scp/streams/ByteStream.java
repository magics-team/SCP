package com.magicteam.scp.streams;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteStream {
    private byte[] buffer;
    private int offset;

    public ByteStream(byte[] data) {
        this.setData(data);
    }

    public boolean isAtAnd() {
        return this.offset >= this.buffer.length;
    }

    public void ensureCapacity(int count) {
        if (this.buffer.length < this.buffer.length + count) {
            this.buffer = ByteBuffer.allocate(this.buffer.length + count).put(this.buffer).array();
        }

    }

    public void write(byte[] data) {
        this.buffer = ByteBuffer.allocate(this.buffer.length + data.length).put(this.buffer).put(data, this.offset, data.length).array();
        this.offset += data.length;
    }

    public byte[] read(int length) {
        byte[] data = new byte[length];
        if (length <= this.buffer.length - this.offset) {
            System.arraycopy(this.buffer, this.offset, data, 0, length);
            this.offset += length;
        }

        return data;
    }

    public void skip(int length) {
        this.offset += length;
    }

    private void write(byte value) {
        this.buffer[this.offset] = value;
        ++this.offset;
    }

    public void writeUnsignedChar(byte value) {
        this.ensureCapacity(1);
        this.write(value);
    }

    public void writeShort(int value) {
        this.ensureCapacity(2);
        this.write((byte)(value & 255));
        this.write((byte)((value & '\uff00') >> 8));
    }

    public void writeInt(int value) {
        this.ensureCapacity(4);
        this.write((byte)(value & 255));
        this.write((byte)((value & '\uff00') >> 8));
        this.write((byte)((value & 16711680) >> 16));
        this.write((byte)((value & -16777216) >> 24));
    }

    public void writeBoolean(boolean value) {
        this.writeUnsignedChar((byte)(value ? 1 : 0));
    }

    public void writeString(String value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            byte[] encodedString = value.getBytes(StandardCharsets.UTF_8);
            this.writeInt(encodedString.length);
            this.write(encodedString);
        }
    }

    public int readUnsignedChar() {
        return this.buffer[this.offset++] & 255;
    }

    public int readShort() {
        return this.readUnsignedChar() | this.readUnsignedChar() << 8;
    }

    public int readInt() {
        return this.readUnsignedChar() | this.readUnsignedChar() << 8 | this.readUnsignedChar() << 16 | this.readUnsignedChar() << 24;
    }

    public Long readLong() {
        return (long) this.readUnsignedChar() | (long) this.readUnsignedChar() << 8 | (long) this.readUnsignedChar() << 16 | (long) this.readUnsignedChar() << 24 | (long) this.readUnsignedChar() << 32 | (long) this.readUnsignedChar() << 40 | (long) this.readUnsignedChar() << 48 | (long) this.readUnsignedChar() << 56;
    }

    public String readString() {
        byte[] data = new byte[256];
        for (int i = 0; i < data.length; i++) {
            byte b = (byte) this.readUnsignedChar();
            if (b == 0) {
                byte[] dst = new byte[i];
                System.arraycopy(data, 0, dst, 0, i);
                return new String(dst);
            }
            data[i] = b;
        }

        return null;
    }

    public boolean readBoolean() {
        return this.readUnsignedChar() == 1;
    }

    public byte[] readByteArray(int count) {
        byte[] array = new byte[count];

        for(int i = 0; i < array.length; ++i) {
            array[i] = (byte)this.readUnsignedChar();
        }

        return array;
    }

    public short[] readShortArray(int count) {
        short[] array = new short[count];

        for(int i = 0; i < array.length; ++i) {
            array[i] = (short)this.readShort();
        }

        return array;
    }

    public int[] readIntArray(int count) {
        int[] array = new int[count];

        for(int i = 0; i < array.length; ++i) {
            array[i] = this.readInt();
        }

        return array;
    }

    public byte[] getBuffer() {
        return ByteBuffer.allocate(this.offset).put(this.buffer, 0, this.offset).array();
    }

    public void setData(byte[] a2) {
        this.buffer = a2;
        this.offset = 0;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return this.buffer.length;
    }
}