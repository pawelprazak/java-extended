/**
 * Basic utility libraries and interfaces.
 *
 * <p>This package is a part of the open-source</p>
 * <a href="https://github.com/pawelprazak/guava-extended">Guava Extended libraries</a>.
 *
 * <h3>Conditional Failures</h3>
 * <ul>
 * <li>{@link com.bluecatcode.common.contract.Preconditions}
 * <li>{@link com.bluecatcode.common.contract.Postconditions}
 * <li>{@link com.bluecatcode.common.contract.Checks}
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
package com.bluecatcode.common.contract;

import javax.annotation.ParametersAreNonnullByDefault;