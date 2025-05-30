package ArthurCode.Campaign_management_app.controller;

import ArthurCode.Campaign_management_app.dto.request.CampaignCreateRequest;
import ArthurCode.Campaign_management_app.dto.request.CampaignUpdateRequest;
import ArthurCode.Campaign_management_app.dto.response.CampaignResponse;
import ArthurCode.Campaign_management_app.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns")
@CrossOrigin(origins = "https://campaign-management-app-production.up.railway.app", allowCredentials = "true")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<Page<CampaignResponse>> getAllFiltered(
            @RequestParam(required = true) Long ownerId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String campaignName,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CampaignResponse> filteredCampaigns = campaignService.filterCampaigns(
                ownerId, productId, status, town, campaignName, keyword, page, size
        );
        return ResponseEntity.ok(filteredCampaigns);
    }

    @GetMapping("/owners/{ownerId}")
    public ResponseEntity<Page<CampaignResponse>> getCampaignByOwnerId(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Page<CampaignResponse> ownerCampaigns = campaignService.getByOwnerId(ownerId, page, size);
        return ResponseEntity.ok(ownerCampaigns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable Long id) {
        CampaignResponse campaignResponse = campaignService.getCampaignResponseById(id);
        return ResponseEntity.ok(campaignResponse);
    }

    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(@Valid @RequestBody CampaignCreateRequest request) {
        CampaignResponse campaignResponse = campaignService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateCampaign(@PathVariable Long id, @Valid @RequestBody CampaignUpdateRequest request) {
        CampaignResponse updatedCampaignResponse = campaignService.update(id, request);
        return ResponseEntity.ok(updatedCampaignResponse);
    }

    @DeleteMapping("/{campaignId}/owners/{ownerId}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long campaignId, @PathVariable Long ownerId) {
        campaignService.delete(campaignId, ownerId);
        return ResponseEntity.noContent().build();
    }
}

