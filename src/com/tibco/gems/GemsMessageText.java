/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  javax.jms.BytesMessage
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.ObjectMessage
 *  javax.jms.StreamMessage
 *  javax.jms.TextMessage
 */
package com.tibco.gems;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class GemsMessageText extends JTextArea {
    public static final int MODE_NONE = 0;
    public static final int MODE_HEXBYTES = 1;
    public static final int MODE_ASCIIBYTES = 2;
    public static final int MODE_TEXT = 3;
    public static final int MODE_XML = 4;
    protected int m_mode = 0;
    protected Message m_msg = null;
    protected JTextArea m_text = null;

    public int getMode() {
        return this.m_mode;
    }

    public void setMode(int n) {
        if (this.m_msg == null) {
            return;
        }
        try {
            if (this.m_msg instanceof BytesMessage) {
                if (n == 1) {
                    BytesMessage bytesMessage = (BytesMessage) this.m_msg;
                    bytesMessage.reset();
                    long l = bytesMessage.getBodyLength();
                    if (l > (long) Gems.getGems().getMaxDisplayBytes()) {
                        System.err.println("Warning: Bytes message display limited to " + Gems.getGems().getMaxDisplayBytes() + " bytes (configurable in gems.props)");
                        l = Gems.getGems().getMaxDisplayBytes();
                    }
                    byte[] arrby = new byte[(int) l];
                    bytesMessage.readBytes(arrby, (int) l);
                    this.setText(StringUtilities.dumpBytes(arrby));
                    this.m_mode = 1;
                } else if (n == 2) {
                    BytesMessage bytesMessage = (BytesMessage) this.m_msg;
                    bytesMessage.reset();
                    long l = bytesMessage.getBodyLength();
                    if (l > (long) Gems.getGems().getMaxDisplayBytes()) {
                        System.err.println("Warning: Bytes message display limited to " + Gems.getGems().getMaxDisplayBytes() + " bytes (configurable in gems.props)");
                        l = Gems.getGems().getMaxDisplayBytes();
                    }
                    byte[] arrby = new byte[(int) l];
                    bytesMessage.readBytes(arrby, (int) l);
                    this.setText(new String(arrby));
                    this.m_mode = 2;
                }
            } else if (this.m_msg instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) this.m_msg;
                if (n == 3) {
                    this.setText(textMessage.getText());
                    this.m_mode = 3;
                } else if (n == 4) {
                    try {
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document document = documentBuilder.parse(new InputSource(new StringReader(textMessage.getText())));
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        transformer.setOutputProperty("indent", "yes");
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        transformer.transform(new DOMSource(document), new StreamResult(byteArrayOutputStream));
                        this.setText(byteArrayOutputStream.toString());
                        this.m_mode = 4;
                    } catch (Exception var3_9) {
                        System.err.println("Exception: " + var3_9.getMessage());
                        this.setText(textMessage.getText());
                        this.m_mode = 3;
                    }
                }
            }
        } catch (Exception var2_5) {
            System.err.println("Exception: " + var2_5.getMessage());
            return;
        }
    }

    public void setMessageText(Message message) {
        this.m_msg = message;
        this.m_mode = 0;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                this.m_mode = 3;

                try {
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    StreamResult result = new StreamResult(new StringWriter());
                    transformer.transform(new StreamSource(new StringReader(textMessage.getText())), result);
                    String xmlString = result.getWriter().toString();

                    this.setText(xmlString);
                } catch (Exception e) {
                    this.setText(textMessage.getText());
                }

            } else if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                this.setText(mapMessage.toString());
            } else if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                bytesMessage.reset();
                long l = bytesMessage.getBodyLength();
                if (l > (long) Gems.getGems().getMaxDisplayBytes()) {
                    System.err.println("Warning: Bytes message display limited to " + Gems.getGems().getMaxDisplayBytes() + " bytes (configurable in gems.props)");
                    l = Gems.getGems().getMaxDisplayBytes();
                }
                byte[] arrby = new byte[(int) l];
                bytesMessage.readBytes(arrby, (int) l);
                this.setText(StringUtilities.dumpBytes(arrby));
                this.m_mode = 1;
            } else if (message instanceof StreamMessage) {
                StreamMessage streamMessage = (StreamMessage) message;
                this.setText(streamMessage.toString());
            } else if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                this.setText(objectMessage.toString());
            }
        } catch (Exception var2_7) {
            System.err.println("Exception: " + var2_7.getMessage());
            return;
        }
    }
}

