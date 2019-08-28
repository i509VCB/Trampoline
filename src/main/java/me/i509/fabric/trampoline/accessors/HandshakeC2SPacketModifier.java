package me.i509.fabric.trampoline.accessors;

public interface HandshakeC2SPacketModifier {
    String getAddress();
    
    void setAddress(String s);
    
    int getPort();
}
