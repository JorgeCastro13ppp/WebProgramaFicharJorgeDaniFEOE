package style

import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*

object AppStyles : StyleSheet() {

    init {

        /* Reset seguro sin romper buenas prácticas */

        universal style {
            property("box-sizing", "border-box")
        }

        "html, body" style {

            fontFamily(
                "Inter",
                "system-ui",
                "sans-serif"
            )

            fontSize(20.px)

            property(
                "font-weight",
                "500"
            )

            property(
                "-webkit-font-smoothing",
                "antialiased"
            )

            margin(0.px)

            padding(0.px)

            height(100.percent)

            backgroundColor(
                rgb(248, 250, 252)
            )
        }

    }

    val textXs = 12.px
    val textSm = 14.px
    val textMd = 16.px
    val textLg = 18.px
    val textXl = 22.px
    val textXxl = 24.px


    val title by style {
        fontSize(26.px)
        fontWeight("bold")
        marginBottom(20.px)
    }

    val username by style {
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        fontSize(14.px)
        fontWeight("bold")
    }

    val logoutButton by style {

        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)

        marginLeft(16.px)
        padding(6.px, 12.px)

        backgroundColor(Color("#e74c3c"))
        color(Color.white)

        borderRadius(6.px)
        border { style(LineStyle.None) }

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#A8331D"))
        }
    }

    val topbarIcon by style {
        width(24.px)
        height(24.px)
        marginRight(8.px)
    }

    /* Login */

    val loginContainer by style {

        minHeight(100.vh)

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)

        alignItems(AlignItems.Center)

        property(
            "background",
            "linear-gradient(135deg, #F8FAFC 0%, #EEF2FF 100%)"
        )
    }

    val loginCard by style {

        width(100.percent)

        maxWidth(420.px)

        backgroundColor(Color.white)

        borderRadius(16.px)

        padding(36.px)

        display(DisplayStyle.Flex)

        flexDirection(FlexDirection.Column)

        gap(18.px)

        property("box-shadow", "0 4px 18px rgba(0,0,0,0.06)")
    }

    val loginTitle by style {

        fontSize(26.px)

        fontWeight("600")

        color(Color("#0F172A"))

        textAlign("center")

        marginBottom(6.px)
    }

    val loginInput by style {

        width(100.percent)

        padding(14.px, 16.px)

        borderRadius(10.px)

        border {

            width(1.px)

            style(LineStyle.Solid)

            color(Color("#CBD5E1"))
        }

        fontSize(20.px)

        property("transition", "border 0.2s ease")

        focus {

            outline("none")

            property("border-color", "#2563EB")
        }
    }

    val loginButton by style {

        width(100.percent)

        fontSize(24.px)
        padding(14.px)

        backgroundColor(Color("#2d3444"))

        color(Color.white)

        borderRadius(10.px)

        border(0.px)

        fontWeight("600")

        cursor("pointer")

        property("transition", "background 0.2s ease")

        self + not(disabled) + hover style {

            backgroundColor(Color("#0F172A"))
        }
    }

    /* Loader */

    val loader by style {

        width(40.px)
        height(40.px)

        borderRadius(50.percent)

        border {
            width(4.px)
            style(LineStyle.Solid)
            color(Color("#1C56FF"))
        }

        property("border-top-color", "transparent")
        property("animation", "spin 1s linear infinite")

        property(
            "@keyframes spin",
            """
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
            """
        )
    }

    val loaderContainer by style {

        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)

        height(60.vh)
    }

    val dashboardGrid by style {

        display(DisplayStyle.Grid)

        property(
            "grid-template-columns",
            "repeat(auto-fit, minmax(220px, 1fr))"
        )

        gap(20.px)

        marginTop(10.px)
    }

    val statCard by style {

        position(Position.Relative)

        padding(22.px)

        borderRadius(16.px)

        backgroundColor(Color.white)

        property("box-shadow", "0 6px 18px rgba(0,0,0,0.05)")

        display(DisplayStyle.Flex)

        flexDirection(FlexDirection.Column)

        gap(6.px)

        property(
            "transition",
            "transform 0.15s ease, box-shadow 0.15s ease"
        )

        self + hover style {

            property("transform", "translateY(-3px)")

            property(
                "box-shadow",
                "0 10px 26px rgba(0,0,0,0.08)"
            )
        }
    }

    val statTitle by style {

        fontSize(14.px)

        color(Color("#64748B"))

        fontWeight("500")
    }

    val statValue by style {

        fontSize(28.px)

        fontWeight("700")

        color(Color("#0F172A"))
    }


    val statAccentBlue by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#386bff"))
    }

    val statAccentGreen by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#22c55e"))
    }

    val statAccentOrange by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#f59e0b"))
    }

    val statAccentRed by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#ef4444"))
    }
    val statAccentYellow by style {
        height(4.px)
        borderRadius(6.px)
        marginBottom(10.px)
        backgroundColor(Color("#FFFF00"))
    }



    val deleteButton by style {



        display(DisplayStyle.Flex)
        gap(6.px)

        property("margin", "0 auto")

        padding(10.px, 16.px)

        backgroundColor(Color("#ef4444"))

        color(Color.white)

        borderRadius(8.px)

        border {
            style(LineStyle.None)
        }

        fontSize(14.px)

        fontWeight("500")

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#A8331D"))
        }


    }



    val table by style {

        width(100.percent)

        display(DisplayStyle.Flex)
        textAlign("center")
        alignItems("center")

        property("border-collapse", "separate")

        property("border-spacing", "0 12px")
    }



    val roleBadge by style {

        padding(4.px, 10.px)

        borderRadius(999.px)

        fontSize(24.px)

        fontWeight("600")

        property("display", "inline-block")

        property("text-transform", "capitalize")
    }

    val roleAdmin by style {

        backgroundColor(Color("#fee2e2"))

        color(Color("#b91c1c"))
    }

    val roleWorker by style {

        backgroundColor(Color("#dbeafe"))

        color(Color("#1d4ed8"))
    }

    val primaryButton by style {

        display(DisplayStyle.Flex)
        gap(6.px)

        property("margin", "0 auto")

        padding(10.px, 16.px)

        backgroundColor(Color("#386bff"))

        color(Color.white)

        borderRadius(8.px)

        border {
            style(LineStyle.None)
        }

        fontSize(14.px)

        fontWeight("500")

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#2f55cc"))
        }
    }

    val badgeFichar by style {

        padding(6.px, 12.px)

        borderRadius(20.px)

        fontSize(20.px)

        fontWeight("600")
    }



    val secondaryButton by style {

        display(DisplayStyle.Flex)

        alignItems(AlignItems.Center)
        property("margin", "0 auto")

        gap(8.px)

        padding(10.px, 16.px)

        backgroundColor(Color("#EEF2FF"))

        color(Color("#1C56FF"))

        borderRadius(10.px)

        border {
            style(LineStyle.None)
        }

        fontSize(14.px)

        fontWeight("600")

        cursor("pointer")

        property("transition", "0.15s ease")

        self + hover style {

            backgroundColor(Color("#E0E7FF"))
        }
    }

    val buttonIcon by style {

        width(18.px)

        height(18.px)
    }

    val filterSelect by style {

        padding(8.px, 14.px)

        borderRadius(8.px)

        border {

            style(LineStyle.Solid)

            width(1.px)

            color(Color("#E2E8F0"))
        }

        backgroundColor(Color.white)

        fontSize(14.px)

        cursor("pointer")

        property("box-shadow", "0 1px 3px rgba(0,0,0,0.04)")
    }

    val badgeWarning by style {

        backgroundColor(Color("#FEF3C7"))
        color(Color("#92400E"))
    }

    val badgeDanger by style {

        backgroundColor(Color("#FEE2E2"))
        color(Color("#991B1B"))
        property("font-variant", "all-small-cap")
    }

    val badgeInfo by style {

        backgroundColor(Color("#DBEAFE"))
        color(Color("#1E40AF"))

        padding(4.px, 10.px)
        borderRadius(6.px)
        fontSize(16.px)

    }

    val rowApproved by style {

        backgroundColor(Color("#e8f8f1"))
    }


    val rowRejected by style {

        backgroundColor(Color("#fdecea"))
    }


    val approveButton by style {

        backgroundColor(Color("#2ecc71"))

        color(Color.white)

        border {
            style(LineStyle.None)
        }

        borderRadius(6.px)

        padding(6.px, 12.px)
        width(50.percent)

    }


    val rejectButton by style {

        backgroundColor(Color("#e74c3c"))

        color(Color.white)

        border {
            style(LineStyle.None)
        }

        borderRadius(6.px)

        padding(6.px, 12.px)

        width(50.percent)
    }

    val badgePendiente by style {

        backgroundColor(Color("#eef2ff"))
        color(Color("#3b5bdb"))

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontWeight("500")
    }

    val badgeAprobado by style {

        backgroundColor(Color("#e8f8f1"))
        color(Color("#27ae60"))

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontWeight("500")
    }

    val badgeRechazado by style {

        backgroundColor(Color("#fdecea"))
        color(Color("#e74c3c"))

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontWeight("500")
    }

    val badgePrimary by style {

        backgroundColor(rgb(59, 130, 246))

        color(Color.white)

        padding(4.px, 10.px)
        borderRadius(6.px)
        fontSize(16.px)

        fontWeight("600")
    }

    val actionCell by style {

        height(6.vh)
        textAlign("center")
        //display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
    }

    val urlCell by style {

        property("word-break","breakword")
        property("overflow-wrap","anywhere")
        property("white-space","normal")
    }

    val statusCell by style {

        height(5.vh)
        textAlign("center")
        //display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
    }

    val actionButtonsGroup by style {

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)

        alignItems(AlignItems.Center)

        gap(8.px)
    }

    val badgePdf by style {

        backgroundColor(Color("#fde68a"))
        color(Color("#92400e"))

        padding(6.px, 12.px)

        borderRadius(10.px)
    }

    val badgeDocumento by style {

        padding(6.px, 14.px)

        borderRadius(12.px)

        fontSize(20.px)

        fontWeight("500")

        property("display", "inline-block")
    }

    val badgeNomina by style {

        backgroundColor(Color("#dbeafe"))

        color(Color("#1e40af"))
    }

    val badgeReconocimiento by style {

        backgroundColor(Color("#fee2e2"))

        color(Color("#991b1b"))
    }

    val badgeFormacion by style {

        backgroundColor(Color("#dcfce7"))

        color(Color("#166534"))
    }

    val badgeEpis by style {

        backgroundColor(Color("#ede9fe"))

        color(Color("#5b21b6"))
    }




    val openButton by style {
        display(DisplayStyle.Flex)

        gap(6.px)

        backgroundColor(Color("#3b82f6"))
        color(Color.white)

        border {
            style(LineStyle.None)
        }

        borderRadius(6.px)

        padding(6.px, 12.px)

        width(100.percent)
    }

    val badgeGreen by style {
        backgroundColor(rgb(220, 252, 231))
        color(rgb(22, 101, 52))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeRed by style {
        backgroundColor(rgb(254, 226, 226))
        color(rgb(153, 27, 27))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeBlue by style {
        backgroundColor(rgb(219, 234, 254))
        color(rgb(30, 64, 175))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeOrange by style {
        backgroundColor(rgb(255, 237, 213))
        color(rgb(154, 52, 18))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    val badgeDefault by style {
        backgroundColor(rgb(229, 231, 235))
        color(rgb(55, 65, 81))
        padding(4.px, 10.px)
        borderRadius(999.px)
    }

    @OptIn(ExperimentalComposeWebApi::class)
    val fadeIn by keyframes {

        from {

            opacity(0)

            transform {
                translateY(10.px)
            }
        }

        to {

            opacity(1)

            transform {
                translateY(0.px)
            }
        }
    }

    @OptIn(ExperimentalComposeWebApi::class)
    val locationIcon by style {

        width(18.px)
        height(18.px)

        cursor("pointer")

        property("transition", "transform 0.15s ease")

        hover {

            transform {
                scale(1.15)
            }
        }
    }



    val filtersRow by style {
        display(DisplayStyle.Flex)
        gap(12.px)
    }

    val filterButton by style {
        padding(10.px, 18.px)
        borderRadius(8.px)
        backgroundColor(Color("#e5e7eb"))
        border(0.px)
        cursor("pointer")
    }

    val filterActive by style {
        padding(10.px, 18.px)
        borderRadius(8.px)
        backgroundColor(Color("#3b82f6"))
        color(Color.white)
        border(0.px)
        cursor("pointer")
    }

    val input by style {
        padding(8.px)
        borderRadius(8.px)
        border {
            style(LineStyle.Solid)
            width(1.px)
            color(Color("#d1d5db"))
        }
    }

    val card by style {
        backgroundColor(Color.white)
        borderRadius(14.px)
        padding(20.px)
        property("box-shadow", "0 4px 12px rgba(0,0,0,0.06)")
    }

    val successButton by style {
        display(DisplayStyle.Flex)
        gap(6.px)
        alignItems(AlignItems.Center)
        padding(6.px, 12.px)
        borderRadius(6.px)
        backgroundColor(Color("#16a34a"))
        color(Color.white)
        border(0.px)
        cursor("pointer")
        property("justify-content", "center")
    }

    val dangerButton by style {
        display(DisplayStyle.Flex)
        gap(6.px)
        alignItems(AlignItems.Center)
        padding(6.px, 12.px)
        borderRadius(6.px)
        backgroundColor(Color("#dc2626"))
        color(Color.white)
        border(0.px)
        cursor("pointer")
        property("justify-content", "center")
    }


    val emptyState by style {
        color(Color("#6b7280"))
        fontSize(14.px)
        marginTop(16.px)
    }

    val badgeSuccess by style {
        backgroundColor(rgb(40, 167, 69))
        color(Color.white)
        padding(4.px, 10.px)
        borderRadius(6.px)
        fontSize(16.px)
    }

    val rowWarning by style {

        backgroundColor(rgb(255, 248, 220))
    }

    val dialogOverlay by style {

        position(Position.Fixed)

        top(0.px)
        left(0.px)

        width(100.percent)
        height(100.percent)

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)

        backgroundColor(rgba(0, 0, 0, 0.45))

        property("backdrop-filter", "blur(4px)")

        property("z-index", "999")
    }

    val dialogBox by style {

        backgroundColor(Color("#FFFFFF"))

        padding(26.px)

        borderRadius(14.px)

        minWidth(320.px)

        maxWidth(420.px)

        property(
            "box-shadow",
            "0 10px 35px rgba(0,0,0,0.18)"
        )

        animation("dialogFadeIn") {
            duration(0.18.s)
        }
    }

    val dialogMessage by style {

        fontSize(16.px)

        color(Color("#1C1C1C"))

        textAlign("center")

        marginBottom(6.px)
    }

    val dialogActions by style {

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)

        gap(14.px)

        marginTop(22.px)

        flexWrap(FlexWrap.Wrap)
    }

    val dialogTitle by style {

        fontSize(20.px)

        fontWeight("600")

        color(Color("#1F3E73"))

        marginBottom(18.px)

        textAlign("center")
    }

    val dialogForm by style {

        display(DisplayStyle.Flex)

        flexDirection(FlexDirection.Column)

        gap(14.px)

        marginBottom(22.px)
    }

    val dialogInput by style {

        padding(10.px)

        borderRadius(8.px)

        border {

            width = 1.px
            style = LineStyle.Solid
            color(Color("#D1D5DB"))
        }

        fontSize(14.px)

        width(100.percent)

        backgroundColor(Color.white)
    }

    val loaderSmall by style {

        width(16.px)

        height(16.px)

        borderRadius(50.percent)

        border {

            width = 2.px
            style = LineStyle.Solid
            color(Color("#FFFFFF"))
        }

        property("border-top", "2px solid transparent")

        property("animation", "spin 0.6s linear infinite")
    }

    val dialogInputError by style {

        border {

            width = 1.5.px
            style = LineStyle.Solid
            color(Color("#DC2626"))
        }
    }

    val inputErrorText by style {

        fontSize(12.px)

        color(Color("#DC2626"))

        marginTop(4.px)
    }

    val primaryButtonDisabled by style {

        opacity(0.6)

        property("cursor", "not-allowed")
    }

    val screenHeaderContainer by style {

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.SpaceBetween)

        alignItems(AlignItems.Center)

        marginBottom(28.px)

        flexWrap(FlexWrap.Wrap)

        gap(16.px)
    }


    val screenHeaderLeft by style {

        display(DisplayStyle.Flex)

        alignItems(AlignItems.Center)

        gap(16.px)

        flexWrap(FlexWrap.Wrap)
    }


    val screenHeaderRight by style {

        display(DisplayStyle.Flex)

        alignItems(AlignItems.Center)

        gap(12.px)

        flexWrap(FlexWrap.Wrap)
    }

    val topbar by style {

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.SpaceBetween)

        alignItems(AlignItems.Center)

        padding(18.px, 26.px)

        backgroundColor(Color("#FFFFFF"))

        property("border-bottom", "1px solid #E2E8F0")

        flexWrap(FlexWrap.Wrap)

        gap(12.px)
    }


    val topbarTitle by style {

        fontSize(36.px)

        fontWeight("600")

        color(Color("#1F3E73"))
    }


    val topbarRight by style {

        display(DisplayStyle.Flex)

        alignItems(AlignItems.Center)

        gap(20.px)

        flexWrap(FlexWrap.Wrap)
    }


    val topbarClock by style {

        fontSize(14.px)

        color(Color("#64748B"))
    }

    val screenContainer by style {

        display(DisplayStyle.Flex)

        flexDirection(FlexDirection.Column)

        gap(12.px)

        width(100.percent)
    }

    val statsGrid by style {

        display(DisplayStyle.Grid)

        property(
            "grid-template-columns",
            "repeat(auto-fit, minmax(220px, 1fr))"
        )

        gap(20.px)

        marginBottom(12.px)
    }

    val tableContainer by style {

        width(100.percent)

        backgroundColor(Color.white)

        borderRadius(12.px)

        padding(20.px)

        property("box-shadow", "0 1px 3px rgba(0,0,0,0.05)")

        property("overflow-x", "auto")
    }

    val tableFlat by style {

        width(100.percent)

        textAlign("center")

        property("border-collapse", "collapse")
        property("table-layout", "fixed")
    }

    val tableCard by style {

        width(100.percent)

        textAlign("center")

        property("border-collapse", "separate")

        property("border-spacing", "0 12px")

        property("table-layout", "fixed")

    }

    val loginLogo by style {

        width(64.px)

        marginBottom(16.px)

        property("margin-left", "auto")

        property("margin-right", "auto")

        display(DisplayStyle.Block)
    }

    val loginError by style {

        color(Color("#DC2626"))

        fontSize(14.px)

        textAlign("center")
    }

    val deleteIcon by style {
        width(16.px)
        height(16.px)
    }
    val aproveIcon by style {
        width(32.px)
        height(32.px)
    }
    val desAproveIcon by style {
        width(32.px)
        height(32.px)
    }


    val layout by style {

        display(DisplayStyle.Flex)
    }


    val content by style {

        flexGrow(1)

        padding(24.px)
    }

    val contentDesktop by style {

        flexGrow(1)

        padding(24.px)

        marginLeft(240.px)

        overflowY("auto")
    }

    val contentTablet by style {

        flexGrow(1)

        padding(24.px)

        marginLeft(72.px)

        overflowY("auto")
    }

    val contentMobile by style {

        flexGrow(1)

        padding(16.px)

        marginLeft(0.px)

        overflowY("auto")
    }


    /*
    ========================
    SIDEBAR DESKTOP
    ========================
    */

    val sidebarDesktop by style {

        position(Position.Fixed)

        top(0.px)
        left(0.px)

        backgroundColor(Color("#2d3444"))

        color(Color.white)

        width(240.px)

        minHeight(100.vh)

        display(DisplayStyle.Flex)

        flexDirection(FlexDirection.Column)

        padding(20.px)

        gap(8.px)   // ← aquí

        property("transition", "all 0.25s ease")
    }


    /*
    ========================
    SIDEBAR TABLET
    (icon-only)
    ========================
    */

    val sidebarTablet by style {

        position(Position.Fixed)

        top(0.px)
        left(0.px)

        backgroundColor(Color("#2d3444"))

        color(Color.white)

        width(72.px)

        minHeight(100.vh)

        display(DisplayStyle.Flex)

        flexDirection(FlexDirection.Column)

        padding(25.px)

        property("transition", "width 0.25s ease")
    }


    /*
    ========================
    SIDEBAR MOBILE DRAWER
    (aparece desde derecha)
    ========================
    */

    val sidebarMobile by style {

        position(Position.Fixed)

        top(0.px)

        left((-260).px)

        width(260.px)

        height(100.vh)

        backgroundColor(Color("#2d3444"))

        color(Color.white)

        display(DisplayStyle.Flex)

        flexDirection(FlexDirection.Column)

        padding(20.px)

        property("z-index", "2000")

        property("transition", "left 0.25s ease")
    }


    /*
    ========================
    SIDEBAR MOBILE OPEN
    ========================
    */

    val sidebarMobileOpen by style {

        property("left", "0px")
    }


    /*
    ========================
    OVERLAY BLUR BACKGROUND
    ========================
    */

    val sidebarOverlay by style {

        position(Position.Fixed)

        top(0.px)

        left(0.px)

        width(100.vw)

        height(100.vh)

        backgroundColor(Color("rgba(0,0,0,0.35)"))

        property("backdrop-filter", "blur(4px)")

        property("z-index", "1500")
    }


    /*
    ========================
    SIDEBAR BUTTON
    (no borde, hover moderno)
    ========================
    */

    val sidebarButton by style {

        display(DisplayStyle.Flex)

        alignItems(AlignItems.Center)

        property("align-self", "stretch")

        property("box-sizing", "border-box")

        gap(14.px)

        padding(14.px, 18.px)

        borderRadius(12.px)

        backgroundColor(Color.transparent)

        color(Color.white)

        cursor("pointer")

        property("transition", "all 0.18s ease")

        self + hover style {

            backgroundColor(Color("#1E293B"))

            property("box-shadow", "inset 3px 0 0 #3B82F6")

            property("transform", "translateX(4px)")
        }
    }


    /*
    ========================
    SIDEBAR BUTTON ACTIVE
    ========================
    */

    val sidebarButtonActive by style {

        backgroundColor(Color("#2563EB"))

        hover {

            backgroundColor(Color("#1D4ED8"))
        }
    }


    /*
    ========================
    SIDEBAR ICON
    (svg blancos)
    ========================
    */

    val sidebarIcon by style {

        width(22.px)

        height(22.px)

        property("filter", "invert(1)")
    }


    /*
    ========================
    SIDEBAR LABEL
    (desaparece en tablet)
    ========================
    */

    val sidebarLabel by style {

        media(mediaMaxWidth(1024.px)) {

            display(DisplayStyle.None)
        }
    }


    /*
    ========================
    HAMBURGER BUTTON
    tablet + mobile
    ========================
    */

    val hamburgerButton by style {

        display(DisplayStyle.Flex)

        justifyContent(JustifyContent.Center)

        alignItems(AlignItems.Center)

        width(48.px)

        height(48.px)

        padding(10.px)

        backgroundColor(Color.transparent)

        border(0.px)

        borderRadius(10.px)

        cursor("pointer")

        property("transition", "background 0.2s ease")

        hover {
            backgroundColor(Color("#E2E8F0"))
        }

        media(mediaMinWidth(1024.px)) {
            self style {
                display(DisplayStyle.None)
            }
        }
    }

    val sidebarCollapsed by style {

        justifyContent(JustifyContent.Center)

        paddingLeft(18.px)

        paddingRight(18.px)

        width(100.percent)
    }


    val skeletonKeyframes by keyframes {

        from {
            property("background-position", "-200px 0")
        }

        to {
            property("background-position", "200px 0")
        }
    }


    val skeletonLoader by style {

        width(30.px)

        height(24.px)

        borderRadius(6.px)

        backgroundImage(
            "linear-gradient(90deg, #F1F5F9 25%, #E2E8F0 37%, #F1F5F9 63%)"
        )

        property("background-size", "400% 100%")

        property(
            "animation",
            "$skeletonKeyframes 1.2s ease-in-out infinite"
        )
    }

    val actionsGroup by style {
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        gap(8.px)

        media(mediaMaxWidth(700.px)) {
            self style {
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
            }
        }
    }

    val textarea by style {

        padding(12.px)

        width(100.percent)

        minHeight(110.px)

        borderRadius(10.px)

        border {
            style(LineStyle.Solid)
            width(1.px)
            color(rgb(210, 214, 220))
        }

        fontSize(14.px)

        property("resize", "vertical")

        outline("none")

        boxSizing("border-box")

        marginTop(14.px)

        marginBottom(10.px)

        backgroundColor(Color.white)

        color(rgb(45, 52, 68))

        self style {
            property("font-family", "inherit")
        }

    }

    val modifyDiv by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

}