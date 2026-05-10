package com.security.fofoqueiro.infrastructure.adapters;

import com.security.fofoqueiro.domain.models.WhiteLabelConfig;
import com.security.fofoqueiro.domain.ports.IWhiteLabelConfigRepositoryPort;
import com.security.fofoqueiro.infrastructure.entities.WhiteLabelConfigEntity;
import com.security.fofoqueiro.infrastructure.mappers.WhiteLabelConfigMapper;
import com.security.fofoqueiro.infrastructure.repositories.SpringDataWhiteLabelConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WhiteLabelConfigRepositoryAdapterImpl implements IWhiteLabelConfigRepositoryPort {

    private final SpringDataWhiteLabelConfigRepository whiteLabelConfigRepository;
    private final WhiteLabelConfigMapper whiteLabelConfigMapper;

    @Override
    public Optional<WhiteLabelConfig> findById(Long id) {
        return whiteLabelConfigRepository.findById(id)
                .map(whiteLabelConfigMapper::toDomain);
    }

    @Override
    public Optional<WhiteLabelConfig> findByTenantId(Long tenantId) {
        return whiteLabelConfigRepository.findByTenantId(tenantId)
                .map(whiteLabelConfigMapper::toDomain);
    }

    @Override
    public WhiteLabelConfig save(WhiteLabelConfig whiteLabelConfig) {
        WhiteLabelConfigEntity whiteLabelConfigEntity = whiteLabelConfigMapper.toEntity(whiteLabelConfig);
        WhiteLabelConfigEntity savedEntity = whiteLabelConfigRepository.save(whiteLabelConfigEntity);
        return whiteLabelConfigMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        whiteLabelConfigRepository.deleteById(id);
    }
}
