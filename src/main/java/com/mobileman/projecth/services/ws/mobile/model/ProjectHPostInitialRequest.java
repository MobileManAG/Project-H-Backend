//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.27 at 05:21:34 PM CET 
//


package com.mobileman.projecth.services.ws.mobile.model;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Birthday" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="InitialSymptomDate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="InitialDiagnosisDate" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="MedicineClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Medicine" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "gender",
    "birthday",
    "initialSymptomDate",
    "initialDiagnosisDate",
    "medicineClass",
    "medicine"
})
@XmlRootElement(name = "ProjectHPostInitialRequest")
public class ProjectHPostInitialRequest {

    @XmlElement(name = "UserName", required = true)
    protected String userName;
    @XmlElement(name = "ActivationCode", required = true)
    protected String activationCode;
    @XmlElement(name = "Gender", required = true)
    protected BigInteger gender;
    @XmlElement(name = "Birthday", required = true)
    protected BigInteger birthday;
    @XmlElement(name = "InitialSymptomDate", required = true)
    protected BigDecimal initialSymptomDate;
    @XmlElement(name = "InitialDiagnosisDate", required = true)
    protected BigDecimal initialDiagnosisDate;
    @XmlElement(name = "MedicineClass", required = true)
    protected String medicineClass;
    @XmlElement(name = "Medicine", required = true)
    protected String medicine;

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
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGender(BigInteger value) {
        this.gender = value;
    }

    /**
     * Gets the value of the birthday property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBirthday() {
        return birthday;
    }

    /**
     * Sets the value of the birthday property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBirthday(BigInteger value) {
        this.birthday = value;
    }

    /**
     * Gets the value of the initialSymptomDate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInitialSymptomDate() {
        return initialSymptomDate;
    }

    /**
     * Sets the value of the initialSymptomDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInitialSymptomDate(BigDecimal value) {
        this.initialSymptomDate = value;
    }

    /**
     * Gets the value of the initialDiagnosisDate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInitialDiagnosisDate() {
        return initialDiagnosisDate;
    }

    /**
     * Sets the value of the initialDiagnosisDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInitialDiagnosisDate(BigDecimal value) {
        this.initialDiagnosisDate = value;
    }

    /**
     * Gets the value of the medicineClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMedicineClass() {
        return medicineClass;
    }

    /**
     * Sets the value of the medicineClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMedicineClass(String value) {
        this.medicineClass = value;
    }

    /**
     * Gets the value of the medicine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMedicine() {
        return medicine;
    }

    /**
     * Sets the value of the medicine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMedicine(String value) {
        this.medicine = value;
    }

}