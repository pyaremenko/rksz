package package1;

import java.nio.ByteBuffer;

public class Package {
    private byte bMagic = 0x13;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16;
    private Message bMsg;
    private short wCrc16_end;
    private byte[] packageBytes;
    public Package(byte[] bytes) {

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (buffer.get() != bMagic)
        {
            throw new RuntimeException();
        }
        this.bSrc = buffer.get();
        this.bPktId = buffer.getLong();
        this.wLen = buffer.getInt();
        this.wCrc16 = buffer.getShort();
        this.bMsg = new Message(buffer, this.wLen);
        this.wCrc16_end = buffer.getShort();
        this.packageBytes = bytes;
    }
    public Package(){
    }

    public byte[] getPackageBytes(){
        return packageBytes;
    }
    @Override
    public String toString() {
        return "package1.Package{" +
                "bMagic=" + bMagic +
                ", bSrc=" + bSrc +
                ", bPktId=" + bPktId +
                ", wLen=" + wLen +
                ", wCrc16=" + wCrc16 +
                ", bMsg=" + bMsg +
                ", wCrc16_end=" + wCrc16_end +
                '}';
    }
}
