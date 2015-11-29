/**
 * Basic utility libraries and interfaces.
 *
 * <p>This package is a part of the open-source</p>
 * <a href="https://github.com/pawelprazak/guava-extended">Guava Extended libraries</a>.

 * <h3>Main goals:</h3>
 * <ul>
 *     <li>Extensions for basic Java types, e.g. RichEnum
 *     <li>Extensions for base Guava types, e.g. ExceptionSupplier
 * </ul>
 *
 * <h2>Contents</h2>
 *
 * <h3>Basic types</h3>
 * <ul>
 * <li>{@link com.bluecatcode.common.base.RichEnum}
 * <li>{@link com.bluecatcode.common.base.ExceptionSupplier}
 * </ul>
 *
 * <h3>Functional</h3>
 *
 * <ul>
 * <li>{@link com.bluecatcode.common.base.Consumer}
 * <li>{@link com.bluecatcode.common.base.Effect}
 * <li>{@link com.bluecatcode.common.base.IsEmpty}
 * <li>{@link com.bluecatcode.common.base.Predicates}
 * </ul>
 *
 * <h3>Conditional Failures</h3>
 * <ul>
 * <li>{@link com.bluecatcode.common.base.Postconditions}
 * <li>{@link com.bluecatcode.common.base.Preconditions}
 * </ul>
 * <p>
 * Summary of the major kinds of runtime checks.
 * </p>
 * <table summary="">
 * <thead>
 * <tr><th>Kind of check</th><th>Description</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>Precondition</td><td>"You messed up (caller)."</td></tr>
 * <tr><td>Postcondition assertion</td><td>"I messed up."</td></tr>
 * <tr><td>Verification</td><td>"Someone I depend on messed up."</td></tr>
 * <tr><td>Test assertion</td><td>"The code I'm testing messed up."</td></tr>
 * <tr><td>Impossible condition</td><td>"Wtf? the world is messed up!"</td></tr>
 * <tr><td>Exceptional result</td><td>"No one messed up, exactly (at least in this VM)."</td></tr>
 * </tbody>
 * </table>
 * <p>
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/ConditionalFailuresExplained">Conditional Failures Explained</a>
 * </p>
 */
@ParametersAreNonnullByDefault
package com.bluecatcode.common.base;

import javax.annotation.ParametersAreNonnullByDefault;