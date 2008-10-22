package org.inqle.data.rdf.jenabean;

import thewebsemantic.Id;
import thewebsemantic.Namespace;
import org.inqle.data.rdf.RDF;

@Namespace(RDF.INQLE)
public class UserAccount {

        private String userName;
        private String password;
        
        @Id
        public String getUserName() {
                return userName;
        }
        public void setUserName(String userName) {
                this.userName = userName;
        }
        public String getPassword() {
                return password;
        }
        public void setPassword(String password) {
                this.password = password;
        }

}