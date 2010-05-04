package org.mobicents.protocols.ss7.tcap.functional;

import org.junit.Test;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

import junit.framework.TestCase;

/**
 * Test for call flow.
 * @author baranowb
 *
 */
public class TCAPFunctionalTest extends TestCase {

	private static final int _WAIT_TIMEOUT = 70000;
	public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
	private PipeSccpProviderImpl provider1;
	private PipeSccpProviderImpl provider2;
	
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	
	private Client client;
	private Server server;

	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.provider1 = new PipeSccpProviderImpl(this);
		this.provider2 = this.provider1.getOther();
		
		//create some fake addresses.
		this.peer1Address = this.provider1.getSccpParameterFactory().getSccpAddress();
		GlobalTitle gt = this.provider1.getSccpParameterFactory().getGlobalTitle100();
		//dont set more, until statics are defined!
		gt.setDigits("5557779");
		this.peer1Address.setGlobalTitle(gt);
		
		this.peer2Address = this.provider1.getSccpParameterFactory().getSccpAddress();
		gt = this.provider1.getSccpParameterFactory().getGlobalTitle100();
		//dont set more, until statics are defined!
		gt.setDigits("5888879");
		this.peer2Address.setGlobalTitle(gt);
		
		//create test classes
		this.client = new Client(provider1,this, peer1Address, peer2Address);
		this.server = new Server(provider2, this, peer2Address, peer1Address);
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	@Test
	public void testSimpleTCWithDialog() throws Exception
	{
		client.start();
		waitForEnd();
		assertTrue("Client side did not finish: "+client.getStatus(),client.isFinished());
		assertTrue("Server side did not finish: "+server.getStatus(),server.isFinished());
	}
	
	
	
	private void waitForEnd()
	{
		try {
			Thread.currentThread().sleep(_WAIT_TIMEOUT);
		} catch (InterruptedException e) {
			fail("Interrupted on wait!");
		}
	}
	
	
	
	
}