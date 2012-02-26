<!--

        Copyright (C) 2012 Johan Grufman

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.

-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fsd="http://www.convx.org/schemas/1.0/fsd">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <xs:schema>
            <xsl:apply-templates/>
        </xs:schema>
    </xsl:template>

    <xsl:template match="/fsd:schema/fsd:root | //fsd:sequence | //fsd:line" priority="1">
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

    <xsl:template match="//fsd:sequence[not(child::node()[@name])] | //fsd:line[not(child::node()[@name])]" priority="2">
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