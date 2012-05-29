/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VlrCapability;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class UpdateLocationRequestImpl extends MobilityMessageImpl implements UpdateLocationRequest {

	protected static final int _TAG_mscNumber = 1;
	protected static final int _TAG_roamingNumber = 0;
	protected static final int _TAG_lmsi = 10;
	protected static final int _TAG_vlrCapability = 6;
	protected static final int _TAG_informPreviousNetworkEntity = 11;
	protected static final int _TAG_csLCSNotSupportedByUE = 12;
	protected static final int _TAG_vGmlcAddress = 2;
	protected static final int _TAG_addInfo = 13;
	protected static final int _TAG_pagingArea = 14;
	protected static final int _TAG_skipSubscriberDataUpdate = 15;
	protected static final int _TAG_restorationIndicator = 16;

	public static final String _PrimitiveName = "UpdateLocationRequest";

	private IMSI imsi;
	private ISDNAddressString mscNumber;
	private ISDNAddressString roamingNumber;
	private ISDNAddressString vlrNumber;
	private LMSI lmsi;
	private MAPExtensionContainer extensionContainer;
	private VlrCapability vlrCapability;
	private boolean informPreviousNetworkEntity;
	private boolean csLCSNotSupportedByUE;
	private GSNAddress vGmlcAddress;
	private ADDInfo addInfo;
	private PagingArea pagingArea;
	private boolean skipSubscriberDataUpdate;
	private boolean restorationIndicator;
	private long mapProtocolVersion;


	public UpdateLocationRequestImpl(long mapProtocolVersion) {
		this.mapProtocolVersion = mapProtocolVersion;
	}

	public UpdateLocationRequestImpl(long mapProtocolVersion, IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString roamingNumber,
			ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, VlrCapability vlrCapability, boolean informPreviousNetworkEntity,
			boolean csLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean skipSubscriberDataUpdate,
			boolean restorationIndicator) {
		this.mapProtocolVersion = mapProtocolVersion;
		this.imsi = imsi;
		this.mscNumber = mscNumber;
		this.roamingNumber = roamingNumber;
		this.vlrNumber = vlrNumber;
		this.lmsi = lmsi;
		this.extensionContainer = extensionContainer;
		this.vlrCapability = vlrCapability;
		this.informPreviousNetworkEntity = informPreviousNetworkEntity;
		this.csLCSNotSupportedByUE = csLCSNotSupportedByUE;
		this.vGmlcAddress = vGmlcAddress;
		this.addInfo = addInfo;
		this.pagingArea = pagingArea;
		this.skipSubscriberDataUpdate = skipSubscriberDataUpdate;
		this.restorationIndicator = restorationIndicator;
	}
	

	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.updateLocation_Request;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.updateLocation;
	}


	@Override
	public IMSI getImsi() {
		return imsi;
	}

	@Override
	public ISDNAddressString getMscNumber() {
		return mscNumber;
	}

	@Override
	public ISDNAddressString getRoamingNumber() {
		return roamingNumber;
	}

	@Override
	public ISDNAddressString getVlrNumber() {
		return vlrNumber;
	}

	@Override
	public LMSI getLmsi() {
		return lmsi;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	@Override
	public VlrCapability getVlrCapability() {
		return vlrCapability;
	}

	@Override
	public boolean getInformPreviousNetworkEntity() {
		return informPreviousNetworkEntity;
	}

	@Override
	public boolean getCsLCSNotSupportedByUE() {
		return csLCSNotSupportedByUE;
	}

	@Override
	public GSNAddress getVGmlcAddress() {
		return vGmlcAddress;
	}

	@Override
	public ADDInfo getADDInfo() {
		return addInfo;
	}

	@Override
	public PagingArea getPagingArea() {
		return pagingArea;
	}

	@Override
	public boolean getSkipSubscriberDataUpdate() {
		return skipSubscriberDataUpdate;
	}

	@Override
	public boolean getRestorationIndicator() {
		return restorationIndicator;
	}

	@Override
	public long getMapProtocolVersion() {
		return mapProtocolVersion;
	}
	

	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		imsi = null;
		mscNumber = null;
		roamingNumber = null;
		vlrNumber = null;
		lmsi = null;
		extensionContainer = null;
		vlrCapability = null;
		informPreviousNetworkEntity = false;
		csLCSNotSupportedByUE = false;
		vGmlcAddress = null;
		addInfo = null;
		pagingArea = null;
		skipSubscriberDataUpdate = false;
		restorationIndicator = false;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			switch (num) {
			case 0:
				// imsi
				if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
							+ ".imsi: Parameter 0 bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				this.imsi = new IMSIImpl();
				((IMSIImpl) this.imsi).decodeAll(ais);
				break;

			case 1:
				// msc-Number or (only for V1 !) roamingNumber
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || (tag != _TAG_mscNumber
						&& (tag != _TAG_roamingNumber || this.mapProtocolVersion != 1)))
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
							+ ".mscNumber or roamingNumber: Parameter 1 bad tag class or tag or not primitive or unsupported in the incoming message version",
							MAPParsingComponentExceptionReason.MistypedParameter);
				if (tag == _TAG_mscNumber) {
					this.mscNumber = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl) this.mscNumber).decodeAll(ais);
				} else {
					this.roamingNumber = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl) this.roamingNumber).decodeAll(ais);
				}
				break;

			case 2:
				// vlr-Number
				if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
							+ ".vlrNumber: Parameter 2 bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				this.vlrNumber = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl) this.vlrNumber).decodeAll(ais);
				break;

			default:
				if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					switch (tag) {
					case _TAG_lmsi: // lmsi
						if (!ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".lmsi: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						this.lmsi = new LMSIImpl();
						((LMSIImpl) this.lmsi).decodeAll(ais);
						break;
					case _TAG_vlrCapability: // vlrCapability
						if (ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".vlrCapability: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						this.vlrCapability = new VlrCapabilityImpl();
						((VlrCapabilityImpl) this.vlrCapability).decodeAll(ais);
						break;
					case _TAG_informPreviousNetworkEntity:
						// informPreviousNetworkEntity
						if (!ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".informPreviousNetworkEntity: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						ais.readNull();
						this.informPreviousNetworkEntity = true;
						break;
					case _TAG_csLCSNotSupportedByUE:
						// csLCSNotSupportedByUE
						if (!ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".csLCSNotSupportedByUE: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						ais.readNull();
						this.csLCSNotSupportedByUE = true;
						break;
					case _TAG_vGmlcAddress: // vGmlcAddress
						if (!ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".vGmlcAddress: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						// TODO: implement it
						ais.advanceElement();
						break;
					case _TAG_addInfo: // addInfo
						if (ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".addInfo: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						// TODO: implement it
						ais.advanceElement();
						break;
					case _TAG_pagingArea: // pagingArea
						if (ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".pagingArea: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						// TODO: implement it
						ais.advanceElement();
						break;
					case _TAG_skipSubscriberDataUpdate:
						// skipSubscriberDataUpdate
						if (!ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".skipSubscriberDataUpdate: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						ais.readNull();
						this.skipSubscriberDataUpdate = true;
						break;
					case _TAG_restorationIndicator:
						// restorationIndicator
						if (!ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".restorationIndicator: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						ais.readNull();
						this.restorationIndicator = true;
						break;

					default:
						ais.advanceElement();
						break;
					}
				} else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

					switch (tag) {
					case Tag.SEQUENCE:
						// extensionContainer
						if (ais.isTagPrimitive())
							throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ".extensionContainer: Parameter extensionContainer is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
						this.extensionContainer = new MAPExtensionContainerImpl();
						((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
						break;
					}
				} else {

					ais.advanceElement();
				}
				break;
			}

			num++;
		}

		if (num < 3)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Needs at least 3 mandatory parameters, found " + num,
					MAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

}
