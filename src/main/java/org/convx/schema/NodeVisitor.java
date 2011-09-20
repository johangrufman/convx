package org.convx.schema;

/**
 * @author johan
 * @since 2011-09-03
 */
public interface NodeVisitor {
    public void visit(ConstantSchemaNode node);
    public void visit(DelimitedSchemaNode node);
    public void visit(NamedSchemaNode node);
    public void visit(RepetitionSchemaNode node);
    public void visit(SequenceSchemaNode node);
}
