/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.DestinationInfo
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 */
package com.tibco.gems;

import java.util.Vector;

import javax.swing.*;

import com.tibco.tibjms.admin.DestinationInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;

public class GemsPurgeQueues
        extends GemsPurgeBase {
    public GemsPurgeQueues(JFrame jFrame, GemsConnectionNode gemsConnectionNode) {
        super(jFrame, gemsConnectionNode, "Queue", ">");
    }

    public GemsPurgeQueues(JFrame jFrame, GemsConnectionNode gemsConnectionNode, String string) {
        super(jFrame, gemsConnectionNode, "Queue", string);
    }

    public void purgeDestinations(Vector vector) {
        if (vector.size() == 0) {
            JOptionPane.showMessageDialog(this.m_frame, "Select queues to purge", "Error", 1);
            return;
        }
        int n = JOptionPane.showConfirmDialog(this, "Purge Selected Queues? Messages will be destroyed", "Purge Queues", 0);
        if (n != 0) {
            return;
        }
        try {
            for (int i = 0; i < vector.size(); ++i) {
                this.m_cn.getJmsAdmin().purgeQueue((String) vector.get(i));
            }
        } catch (TibjmsAdminException var3_4) {
            JOptionPane.showMessageDialog(this.m_frame, var3_4.getMessage(), "Error", 1);
        }
    }

    public DestinationInfo[] getDestinationInfo() {
        try {
            try {
                return this.m_cn.getJmsAdmin().getQueues(this.m_pattern.getText(), this.m_pattern.getText().startsWith("$TMP$") ? 4 : 3);
            } catch (Throwable var1_1) {
                return this.m_cn.getJmsAdmin().getQueues(this.m_pattern.getText());
            }
        } catch (TibjmsAdminException var1_2) {
            JOptionPane.showMessageDialog(this.m_frame, var1_2.getMessage(), "Error", 1);
            return null;
        }
    }
}

