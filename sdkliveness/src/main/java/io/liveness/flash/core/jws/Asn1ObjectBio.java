package io.liveness.flash.core.jws;

import java.io.IOException;
import java.math.BigInteger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Phonglx
 */
 class Asn1ObjectBio {

	protected final int type;
	protected final int length;
	protected final byte[] value;
	protected final int tag;

	/**
	 * Construct a ASN.1 TLV. The TLV could be either a constructed or primitive
	 * entity.
	 *
	 * <p/>
	 * The first byte in DER encoding is made of following fields,
	 * <pre>
	 *-------------------------------------------------
	 *|Bit 8|Bit 7|Bit 6|Bit 5|Bit 4|Bit 3|Bit 2|Bit 1|
	 *-------------------------------------------------
	 *|  Class    | CF  |     +      Type             |
	 *-------------------------------------------------
	 * </pre>
	 * <ul>
	 * <li>Class: Universal, Application, Context or Private
	 * <li>CF: Constructed flag. If 1, the field is constructed.
	 * <li>Type: This is actually called tag in ASN.1. It indicates data type
	 * (Integer, String) or a construct (sequence, choice, set).
	 * </ul>
	 *
	 * @param tag Tag or Identifier
	 * @param length Length of the field
	 * @param value Encoded octet string for the field.
	 */
	public Asn1ObjectBio(int tag, int length, byte[] value) {
		this.tag = tag;
		this.type = tag & 0x1F;
		this.length = length;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public byte[] getValue() {
		return value;
	}

	public boolean isConstructed() {
		return (tag & DerParserBio.CONSTRUCTED) == DerParserBio.CONSTRUCTED;
	}

	/**
	 * For constructed field, return a parser for its content.
	 *
	 * @return A parser for the construct.
	 * @throws IOException
	 */
	public DerParserBio getParser() throws IOException {
		if (!isConstructed()) {
			throw new IOException("Invalid DER: can't parse primitive entity"); //$NON-NLS-1$
		}
		return new DerParserBio(value);
	}

	/**
	 * Get the value as integer
	 *
	 * @return BigInteger
	 * @throws IOException
	 */
	public BigInteger getInteger() throws IOException {
		if (type != DerParserBio.INTEGER) {
			throw new IOException("Invalid DER: object is not integer"); //$NON-NLS-1$
		}
		return new BigInteger(value);
	}

	/**
	 * Get value as string. Most strings are treated as Latin-1.
	 *
	 * @return Java string
	 * @throws IOException
	 */
	public String getString() throws IOException {

		String encoding;

		switch (type) {

			// Not all are Latin-1 but it's the closest thing
			case DerParserBio.NUMERIC_STRING:
			case DerParserBio.PRINTABLE_STRING:
			case DerParserBio.VIDEOTEX_STRING:
			case DerParserBio.IA5_STRING:
			case DerParserBio.GRAPHIC_STRING:
			case DerParserBio.ISO646_STRING:
			case DerParserBio.GENERAL_STRING:
				encoding = "ISO-8859-1"; //$NON-NLS-1$
				break;

			case DerParserBio.BMP_STRING:
				encoding = "UTF-16BE"; //$NON-NLS-1$
				break;

			case DerParserBio.UTF8_STRING:
				encoding = "UTF-8"; //$NON-NLS-1$
				break;

			case DerParserBio.UNIVERSAL_STRING:
				throw new IOException("Invalid DER: can't handle UCS-4 string"); //$NON-NLS-1$

			default:
				throw new IOException("Invalid DER: object is not a string"); //$NON-NLS-1$
		}

		return new String(value, encoding);
	}
}

