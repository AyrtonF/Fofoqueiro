package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.Gateway;
import com.security.fofoqueiro.domain.ports.IGatewayRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.GatewayEntity;
import com.security.fofoqueiro.infrastructure.mappers.GatewayMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataGatewayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GatewayRepositoryAdapterImpl implements IGatewayRepositoryPort {

    private final SpringDataGatewayRepository gatewayRepository;
    private final GatewayMapper gatewayMapper;

    @Override
    public Optional<Gateway> findById(Long id) {
        return gatewayRepository.findById(id)
                .map(gatewayMapper::toDomain);
    }

    @Override
    public Gateway save(Gateway gateway) {
        GatewayEntity gatewayEntity = gatewayMapper.toEntity(gateway);
        GatewayEntity savedEntity = gatewayRepository.save(gatewayEntity);
        return gatewayMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        gatewayRepository.deleteById(id);
    }

    @Override
    public List<Gateway> findByTenantId(Long tenantId) {
        return gatewayRepository.findByTenantId(tenantId)
                .stream()
                .map(gatewayMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Gateway> findByTenantIdAndStatus(Long tenantId, String status) {
        return gatewayRepository.findByTenantIdAndStatus(tenantId, status)
                .stream()
                .map(gatewayMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Gateway> findByIpAddress(String ipAddress) {
        return gatewayRepository.findByIpAddress(ipAddress)
                .map(gatewayMapper::toDomain);
    }
}
