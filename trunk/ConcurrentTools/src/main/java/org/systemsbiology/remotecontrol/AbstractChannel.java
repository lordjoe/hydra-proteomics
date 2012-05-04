package org.systemsbiology.remotecontrol;

import com.jcraft.jsch.*;

/**
 * org.systemsbiology.remotecontrol.AbstractChannel
 * User: steven
 * Date: Jun 2, 2010
 */
public abstract class AbstractChannel {
    public static final AbstractChannel[] EMPTY_ARRAY = {};
    protected final Channel m_Channel;
    private boolean m_Connected;
    private final RemoteSession m_Session;

    public AbstractChannel(final RemoteSession pSession) {
        m_Session = pSession;
        m_Channel = buildChannel();
    }

    private Channel buildChannel() {
        try {
            RemoteSession session = getSession();
            Session session1 = session.getSession();
            return session1.openChannel(getChannelType());
        }
        catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    protected Channel getBaseChannel() {
        return m_Channel;
    }

    protected abstract Class<? extends Channel> getChannelClass();

    protected abstract String getChannelType();


    public RemoteSession getSession() {
        return m_Session;
    }

    public boolean isConnected() {
        return m_Connected;
    }

    public void setConnected(final boolean pConnected) {
        if (m_Connected == pConnected)
            return;
        if (pConnected) {
            try {
                m_Channel.connect();
            }
            catch (JSchException e) {
                throw new RuntimeException(e);
            }
            m_Connected = true;
        }
        else {
            m_Channel.disconnect();
            m_Connected = false;

        }
        m_Connected = pConnected;
    }

}
