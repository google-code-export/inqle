package org.inqle.test.restlet;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;


public class DocumentServerResource extends ServerResource {

	@Get
	public Representation toJson()throws JSONException {
		System.out.println("toJson()...");
//		JSONObject DocumentElt = new JSONObject();
//		DocumentElt.put("status", "received");
//		DocumentElt.put("subject", "Message to self");
//		DocumentElt.put("content", "Doh!");
//		DocumentElt.put("accountRef", new Reference(getReference(), "..").getTargetRef().toString());
//		return new JsonRepresentation (DocumentElt);
		Document doc = new Document();
		doc.setTitle("Howdy!");
		return new JacksonRepresentation<Document> (doc);
	}
	
	@Put
	public void store(JsonRepresentation rep) throws JSONException, IOException {
		System.out.println("store()...");
//		JSONObject DocumentElt = DocumentRep.getJsonObject();
//		System.out.println("Status: " + DocumentElt.getString("status"));
//		System.out.println("Subject: " + DocumentElt.getString("subject"));
//		System.out.println("Content: " + DocumentElt.getString("content"));
//		System.out.println("Account URI: " + DocumentElt.getString("accountRef"));
		
		JacksonRepresentation<Document> questionRep = new JacksonRepresentation<Document>(
				rep, Document.class);
		Document question = questionRep.getObject();
		System.out.println("title: " + question.getTitle());
	
	}
	
}
