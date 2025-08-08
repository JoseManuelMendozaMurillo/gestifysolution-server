package com.ventuit.adminstrativeapp.products.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.CreateProductImageDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.dto.UpdateProductDto;
import com.ventuit.adminstrativeapp.products.mappers.ProductsMapper;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.models.ProductsCategoriesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsImagesModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
import com.ventuit.adminstrativeapp.products.repositories.ProductsCategoriesRepository;
import com.ventuit.adminstrativeapp.core.services.implementations.CrudServiceImpl;
import com.ventuit.adminstrativeapp.shared.dto.FileUploadDto;
import com.ventuit.adminstrativeapp.shared.exceptions.EntityNotFoundException;
import com.ventuit.adminstrativeapp.shared.models.FilesModel;
import com.ventuit.adminstrativeapp.shared.services.implementations.FilesServiceImpl;
import com.ventuit.adminstrativeapp.storage.minio.MinioProvider;
import com.ventuit.adminstrativeapp.storage.minio.services.MinioService;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class ProductsService extends
        CrudServiceImpl<CreateProductDto, UpdateProductDto, ListProductDto, ProductsModel, Integer, ProductsMapper, ProductsRepository> {

    @Autowired
    private ProductsCategoriesRepository categoriesRepository;
    @Autowired
    private FilesServiceImpl filesService;
    @Autowired
    private MinioProvider minioProvider;
    @Autowired
    private MinioService minioService;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductsService(ProductsRepository repository, ProductsMapper mapper) {
        super(repository, mapper);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductDto create(CreateProductDto createDto) {
        // TODO: Test this endpoint
        creatingDtoValidator.validate(createDto);

        List<ProductsImagesModel> productImages = new ArrayList<>();
        List<FilesModel> uploadedFiles = new ArrayList<>();
        ProductsModel product = mapper.toEntity(createDto);
        try {
            // Upload images
            for (CreateProductImageDto image : createDto.getImages()) {
                FileUploadDto fileUploadDto = FileUploadDto.builder()
                        .fileInputStream(new ByteArrayInputStream(image.getImage().getBytes()))
                        .fileSize((long) image.getImage().getBytes().length)
                        .contentType(image.getImage().getContentType())
                        .originalFileName(image.getImage().getOriginalFilename())
                        .path("products/images")
                        .provider("minio")
                        .bucket(minioProvider.getBucketName())
                        .build();
                FilesModel productImage = filesService.uploadFile(fileUploadDto);
                uploadedFiles.add(productImage);

                ProductsImagesModel productsImagesModel = ProductsImagesModel.builder()
                        .file(productImage)
                        .portrait(image.getPortrait())
                        .build();
                productImages.add(productsImagesModel);
            }
            // Save the product
            ProductsModel productSaved = repository.save(product);

            // Associate the product with the images and save all
            for (ProductsImagesModel img : productImages) {
                img.setProduct(productSaved);
                productSaved.getImages().add(img);
            }
            // productsImagesRepository.saveAll(productImages);
            productSaved = repository.save(productSaved);

            return mapper.toShowDto(productSaved);
        } catch (IOException e) {
            deleteUploadedFiles(uploadedFiles);
            throw new RuntimeException("Error uploading product images", e);
        } catch (Exception e) {
            deleteUploadedFiles(uploadedFiles);
            throw e;
        }
    }

    private void deleteUploadedFiles(List<FilesModel> files) {
        for (FilesModel file : files) {
            String productImagePath = file.getFilesPaths().getPath() + "/" + file.getFileKey();
            minioService.deleteFile(productImagePath);
        }
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public ListProductDto update(Integer id, UpdateProductDto updateDto) {
        ProductsModel entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        mapper.updateFromDto(updateDto, entity);
        if (updateDto.getCategoryId() != null) {
            ProductsCategoriesModel category = categoriesRepository.findById(updateDto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            entity.setCategory(category);
        }
        ProductsModel saved = repository.save(entity);
        return mapper.toShowDto(saved);
    }
}
