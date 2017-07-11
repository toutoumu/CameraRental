
package com.dsfy.refund;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.dsfy.refund package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ProcessRefundResponse_QNAME = new QName("http://www.sdo.com/mas/api/refund/", "processRefundResponse");
    private final static QName _ProcessRefund_QNAME = new QName("http://www.sdo.com/mas/api/refund/", "processRefund");
    private final static QName _MasAPIException_QNAME = new QName("http://www.sdo.com/mas/api/refund/", "MasAPIException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.dsfy.refund
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessRefundResponse }
     */
    public ProcessRefundResponse createProcessRefundResponse() {
        return new ProcessRefundResponse();
    }

    /**
     * Create an instance of {@link ProcessRefund }
     */
    public ProcessRefund createProcessRefund() {
        return new ProcessRefund();
    }

    /**
     * Create an instance of {@link MasAPIException }
     */
    public MasAPIException createMasAPIException() {
        return new MasAPIException();
    }

    /**
     * Create an instance of {@link ReturnInfo }
     */
    public ReturnInfo createReturnInfo() {
        return new ReturnInfo();
    }

    /**
     * Create an instance of {@link Extension }
     */
    public Extension createExtension() {
        return new Extension();
    }

    /**
     * Create an instance of {@link RefundRequest }
     */
    public RefundRequest createRefundRequest() {
        return new RefundRequest();
    }

    /**
     * Create an instance of {@link Sender }
     */
    public Sender createSender() {
        return new Sender();
    }

    /**
     * Create an instance of {@link Signature }
     */
    public Signature createSignature() {
        return new Signature();
    }

    /**
     * Create an instance of {@link Service }
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link RefundResponse }
     */
    public RefundResponse createRefundResponse() {
        return new RefundResponse();
    }

    /**
     * Create an instance of {@link Header }
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessRefundResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.sdo.com/mas/api/refund/", name = "processRefundResponse")
    public JAXBElement<ProcessRefundResponse> createProcessRefundResponse(ProcessRefundResponse value) {
        return new JAXBElement<ProcessRefundResponse>(_ProcessRefundResponse_QNAME, ProcessRefundResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessRefund }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.sdo.com/mas/api/refund/", name = "processRefund")
    public JAXBElement<ProcessRefund> createProcessRefund(ProcessRefund value) {
        return new JAXBElement<ProcessRefund>(_ProcessRefund_QNAME, ProcessRefund.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MasAPIException }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.sdo.com/mas/api/refund/", name = "MasAPIException")
    public JAXBElement<MasAPIException> createMasAPIException(MasAPIException value) {
        return new JAXBElement<MasAPIException>(_MasAPIException_QNAME, MasAPIException.class, null, value);
    }

}
