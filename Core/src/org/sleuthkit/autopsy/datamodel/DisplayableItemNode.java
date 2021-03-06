/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011-2017 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.datamodel;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * An abstract base class for nodes that are eligible for display in the tree
 * view or results view. Capabilitites of a DisplayableItemNode include
 * accepting a DisplayableItemNodeVisitor, indicating whether or not the node is
 * a leaf node, providing an item type string suitable for use as a key, and
 * storing information about a child node that is to be selected if the node is
 * selected in the tree view.
 */
public abstract class DisplayableItemNode extends AbstractNode {

    /*
     * An item type shared by DisplayableItemNodes that can be the parents of
     * file nodes.
     */
    final static String FILE_PARENT_NODE_KEY = "orgsleuthkitautopsydatamodel" + "FileTypeParentNode";

    /**
     * Gets the file, if any, linked to an artifact via a TSK_PATH_ID attribute
     *
     * @param artifact The artifact.
     *
     * @return An AbstractFile or null.
     *
     * @throws TskCoreException
     */
    static AbstractFile findLinked(BlackboardArtifact artifact) throws TskCoreException {
        BlackboardAttribute pathIDAttribute = artifact.getAttribute(new BlackboardAttribute.Type(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_PATH_ID));
        if (pathIDAttribute != null) {
            long contentID = pathIDAttribute.getValueLong();
            if (contentID != -1) {
                return artifact.getSleuthkitCase().getAbstractFileById(contentID);
            }
        }
        return null;
    }

    /**
     * Constructs a node that is eligible for display in the tree view or
     * results view. Capabilitites include accepting a
     * DisplayableItemNodeVisitor, indicating whether or not the node is a leaf
     * node, providing an item type string suitable for use as a key, and
     * storing information about a child node that is to be selected if the node
     * is selected in the tree view.
     *
     * @param children The Children object for the node.
     */
    public DisplayableItemNode(Children children) {
        super(children);
    }

    /**
     * Constructs a node that is eligible for display in the tree view or
     * results view. Capabilitites include accepting a
     * DisplayableItemNodeVisitor, indicating whether or not the node is a leaf
     * node, providing an item type string suitable for use as a key, and
     * storing information about a child node that is to be selected if the node
     * is selected in the tree view.
     *
     * @param children The Children object for the node.
     * @param lookup   The Lookup object for the node.
     */
    public DisplayableItemNode(Children children, Lookup lookup) {
        super(children, lookup);
    }

    /**
     * Accepts a DisplayableItemNodeVisitor.
     *
     * @param <T>     The type parameter of the visitor.
     * @param visitor The visitor.
     *
     * @return
     */
    public abstract <T> T accept(DisplayableItemNodeVisitor<T> visitor);

    /**
     * Indicates whether or not the node is a leaf node.
     *
     * @return True or false.
     */
    public abstract boolean isLeafTypeNode();

    /**
     * Gets the item type string of the node, suitable for use as a key.
     *
     * @return A String representing the item type of node.
     */
    public abstract String getItemType();

}
