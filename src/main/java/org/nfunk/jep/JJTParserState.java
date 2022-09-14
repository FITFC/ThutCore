/*****************************************************************************
 * JEP - Java Math Expression Parser 2.3.1
 * January 26 2006
 * (c) Copyright 2004, Nathan Funk and Richard Morris
 * See LICENSE.txt for license information.
 *****************************************************************************/
/* Generated By:JJTree: Do not edit this line. JJTParserState.java */

package org.nfunk.jep;

@SuppressWarnings({ "rawtypes", "unchecked" })
class JJTParserState
{
    private final java.util.Stack nodes;
    private final java.util.Stack marks;

    private int     sp;          		// number of nodes on stack
    private int     mk;          		// current mark
    private boolean node_created;

    JJTParserState()
    {
        this.nodes = new java.util.Stack();
        this.marks = new java.util.Stack();
        this.sp = 0;
        this.mk = 0;
    }

    void clearNodeScope(Node n)
    {
        while (this.sp > this.mk)
            this.popNode();
        this.mk = ((Integer) this.marks.pop()).intValue();
    }

    /*
     * A conditional node is constructed if its condition is true. All
     * the nodes that have been pushed since the node was opened are
     * made children of the the conditional node, which is then pushed
     * on to the stack. If the condition is false the node is not
     * constructed and they are left on the stack.
     */
    void closeNodeScope(Node n, boolean condition)
    {
        if (condition)
        {
            int a = this.nodeArity();
            this.mk = ((Integer) this.marks.pop()).intValue();
            while (a-- > 0)
            {
                final Node c = this.popNode();
                c.jjtSetParent(n);
                n.jjtAddChild(c, a);
            }
            n.jjtClose();
            this.pushNode(n);
            this.node_created = true;
        }
        else
        {
            this.mk = ((Integer) this.marks.pop()).intValue();
            this.node_created = false;
        }
    }

    /*
     * A definite node is constructed from a specified number of
     * children. That number of nodes are popped from the stack and
     * made the children of the definite node. Then the definite node
     * is pushed on to the stack.
     */
    void closeNodeScope(Node n, int num)
    {
        this.mk = ((Integer) this.marks.pop()).intValue();
        while (num-- > 0)
        {
            final Node c = this.popNode();
            c.jjtSetParent(n);
            n.jjtAddChild(c, num);
        }
        n.jjtClose();
        this.pushNode(n);
        this.node_created = true;
    }

    /*
     * Returns the number of children on the stack in the current node
     * scope.
     */
    int nodeArity()
    {
        return this.sp - this.mk;
    }

    /*
     * Determines whether the current node was actually closed and
     * pushed. This should only be called in the final user action of a
     * node scope.
     */
    boolean nodeCreated()
    {
        return this.node_created;
    }

    void openNodeScope(Node n)
    {
        this.marks.push(Integer.valueOf(this.mk));
        this.mk = this.sp;
        n.jjtOpen();
    }

    /* Returns the node currently on the top of the stack. */
    Node peekNode()
    {
        return (Node) this.nodes.peek();
    }

    /*
     * Returns the node on the top of the stack, and remove it from the
     * stack.
     */
    Node popNode()
    {
        if (--this.sp < this.mk) this.mk = ((Integer) this.marks.pop()).intValue();
        return (Node) this.nodes.pop();
    }

    /* Pushes a node on to the stack. */
    void pushNode(Node n)
    {
        this.nodes.push(n);
        ++this.sp;
    }

    /*
     * Call this to reinitialize the node stack. It is called
     * automatically by the parser's ReInit() method.
     */
    void reset()
    {
        this.nodes.removeAllElements();
        this.marks.removeAllElements();
        this.sp = 0;
        this.mk = 0;
    }

    /*
     * Returns the root node of the AST. It only makes sense to call
     * this after a successful parse.
     */
    Node rootNode()
    {
        return (Node) this.nodes.elementAt(0);
    }
}