<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fsd="http://www.convx.org/schemas/1.0/fsd">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <xs:schema>
            <xsl:apply-templates/>
        </xs:schema>
    </xsl:template>

    <xsl:template match="/fsd:schema/fsd:root | //fsd:sequence" priority="1">
        <xs:element name="{@name}">
            <xsl:if test="@maxOccurs">
                <xsl:attribute name="maxOccurs"><xsl:value-of select="@maxOccurs"/></xsl:attribute>
            </xsl:if>
            <xs:complexType>
                <xs:sequence>
                    <xsl:apply-templates/>
                </xs:sequence>
            </xs:complexType>
        </xs:element>
    </xsl:template>

    <xsl:template match="//fsd:sequence[not(child::node()[@name])]" priority="2">
        <xsl:variable name="fieldNode" select="fsd:field"/>
        <xsl:choose>
            <xsl:when test="$fieldNode/@ref">
                <xs:element name="{@name}" type="{$fieldNode/@ref}"/>
            </xsl:when>
            <xsl:otherwise>
                <xs:element name="{@name}" type="xs:string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="//fsd:element[@name]">
        <xs:element name="{@name}" type="{@ref}"/>
    </xsl:template>

    <xsl:template match="/fsd:schema/fsd:field[@id]">
        <xs:simpleType name="{@id}">
            <xs:restriction base="xs:string">
            </xs:restriction>
        </xs:simpleType>
    </xsl:template>

    <xsl:template match="/fsd:schema/fsd:root//fsd:field[@name]">
        <xs:element name="{@name}" type="xs:string"/>
    </xsl:template>

</xsl:stylesheet>