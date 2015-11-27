//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.27 at 05:21:34 PM CET 
//


package com.mobileman.projecth.services.ws.mobile.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ActivationCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LogDate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="HAQ1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ1Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ2Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ3Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ3SUB" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ3SUBValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ4" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ4Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ5" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ5Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ6" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ6Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ7" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HAQ7Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userName",
    "activationCode",
    "logDate",
    "haq1",
    "haq1Value",
    "haq2",
    "haq2Value",
    "haq3",
    "haq3Value",
    "haq3SUB",
    "haq3SUBValue",
    "haq4",
    "haq4Value",
    "haq5",
    "haq5Value",
    "haq6",
    "haq6Value",
    "haq7",
    "haq7Value"
})
@XmlRootElement(name = "ProjectHPostRequest")
public class ProjectHPostRequest {

    @XmlElement(name = "UserName", required = true)
    protected String userName;
    @XmlElement(name = "ActivationCode", required = true)
    protected String activationCode;
    @XmlElement(name = "LogDate", required = true)
    protected BigDecimal logDate;
    @XmlElement(name = "HAQ1", required = true)
    protected String haq1;
    @XmlElement(name = "HAQ1Value", required = true)
    protected String haq1Value;
    @XmlElement(name = "HAQ2", required = true)
    protected String haq2;
    @XmlElement(name = "HAQ2Value", required = true)
    protected String haq2Value;
    @XmlElement(name = "HAQ3", required = true)
    protected String haq3;
    @XmlElement(name = "HAQ3Value", required = true)
    protected String haq3Value;
    @XmlElement(name = "HAQ3SUB", required = true)
    protected String haq3SUB;
    @XmlElement(name = "HAQ3SUBValue", required = true)
    protected String haq3SUBValue;
    @XmlElement(name = "HAQ4", required = true)
    protected String haq4;
    @XmlElement(name = "HAQ4Value", required = true)
    protected String haq4Value;
    @XmlElement(name = "HAQ5", required = true)
    protected String haq5;
    @XmlElement(name = "HAQ5Value", required = true)
    protected String haq5Value;
    @XmlElement(name = "HAQ6", required = true)
    protected String haq6;
    @XmlElement(name = "HAQ6Value", required = true)
    protected String haq6Value;
    @XmlElement(name = "HAQ7", required = true)
    protected String haq7;
    @XmlElement(name = "HAQ7Value", required = true)
    protected String haq7Value;

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the activationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivationCode() {
        return activationCode;
    }

    /**
     * Sets the value of the activationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivationCode(String value) {
        this.activationCode = value;
    }

    /**
     * Gets the value of the logDate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLogDate() {
        return logDate;
    }

    /**
     * Sets the value of the logDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLogDate(BigDecimal value) {
        this.logDate = value;
    }

    /**
     * Gets the value of the haq1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ1() {
        return haq1;
    }

    /**
     * Sets the value of the haq1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ1(String value) {
        this.haq1 = value;
    }

    /**
     * Gets the value of the haq1Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ1Value() {
        return haq1Value;
    }

    /**
     * Sets the value of the haq1Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ1Value(String value) {
        this.haq1Value = value;
    }

    /**
     * Gets the value of the haq2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ2() {
        return haq2;
    }

    /**
     * Sets the value of the haq2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ2(String value) {
        this.haq2 = value;
    }

    /**
     * Gets the value of the haq2Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ2Value() {
        return haq2Value;
    }

    /**
     * Sets the value of the haq2Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ2Value(String value) {
        this.haq2Value = value;
    }

    /**
     * Gets the value of the haq3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ3() {
        return haq3;
    }

    /**
     * Sets the value of the haq3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ3(String value) {
        this.haq3 = value;
    }

    /**
     * Gets the value of the haq3Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ3Value() {
        return haq3Value;
    }

    /**
     * Sets the value of the haq3Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ3Value(String value) {
        this.haq3Value = value;
    }

    /**
     * Gets the value of the haq3SUB property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ3SUB() {
        return haq3SUB;
    }

    /**
     * Sets the value of the haq3SUB property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ3SUB(String value) {
        this.haq3SUB = value;
    }

    /**
     * Gets the value of the haq3SUBValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ3SUBValue() {
        return haq3SUBValue;
    }

    /**
     * Sets the value of the haq3SUBValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ3SUBValue(String value) {
        this.haq3SUBValue = value;
    }

    /**
     * Gets the value of the haq4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ4() {
        return haq4;
    }

    /**
     * Sets the value of the haq4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ4(String value) {
        this.haq4 = value;
    }

    /**
     * Gets the value of the haq4Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ4Value() {
        return haq4Value;
    }

    /**
     * Sets the value of the haq4Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ4Value(String value) {
        this.haq4Value = value;
    }

    /**
     * Gets the value of the haq5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ5() {
        return haq5;
    }

    /**
     * Sets the value of the haq5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ5(String value) {
        this.haq5 = value;
    }

    /**
     * Gets the value of the haq5Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ5Value() {
        return haq5Value;
    }

    /**
     * Sets the value of the haq5Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ5Value(String value) {
        this.haq5Value = value;
    }

    /**
     * Gets the value of the haq6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ6() {
        return haq6;
    }

    /**
     * Sets the value of the haq6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ6(String value) {
        this.haq6 = value;
    }

    /**
     * Gets the value of the haq6Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ6Value() {
        return haq6Value;
    }

    /**
     * Sets the value of the haq6Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ6Value(String value) {
        this.haq6Value = value;
    }

    /**
     * Gets the value of the haq7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ7() {
        return haq7;
    }

    /**
     * Sets the value of the haq7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ7(String value) {
        this.haq7 = value;
    }

    /**
     * Gets the value of the haq7Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHAQ7Value() {
        return haq7Value;
    }

    /**
     * Sets the value of the haq7Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHAQ7Value(String value) {
        this.haq7Value = value;
    }

}