package com.lge.stark.eddard.gateway;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.Settings;
import com.lge.stark.eddard.model.Fault;

public class ElasticsearchGateway {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ElasticsearchGateway.class);

	public static Client getClient() throws FaultException {

		org.elasticsearch.common.settings.Settings settings = org.elasticsearch.common.settings.Settings
				.settingsBuilder().put("cluster.name", Settings.SELF.getProperty("elasticsearch.clustername")).build();

		Client client;
		try {
			client = TransportClient.builder().settings(settings).build().addTransportAddress(
					new InetSocketTransportAddress(InetAddress.getByName(Settings.SELF.getProperty("elasticsearch.ip")),
							9300));
		}
		catch (UnknownHostException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		return client;
	}
}